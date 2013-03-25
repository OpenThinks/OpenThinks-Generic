/**
 * Extension for WebView on Android, which doesn't support WebSocket protocol yet.
 */
(function () {
    if (!window.WebSocket) {
        // window object
        var global = window;

        // WebSocket Object. All listener methods are cleaned up!
        var WebSocket = global.WebSocket = function (url) {
            // get a new websocket object from factory (check com.openthinks.websocket.WebViewWebSocketFactory.java)
            this.socket = WebViewWebSocketFactory.createInstance(url);
            // store in registry
            if (this.socket) {
                WebSocket.store[this.socket.getId()] = this;
            } else {
                throw new Error('Websocket instantiation failed! Address might be wrong.');
            }
        };

        // storage to hold websocket object for later invoke of event methods
        WebSocket.store = {};

        // static event methods to call event methods on target websocket objects
        WebSocket.onmessage = function (evt) {
            WebSocket.store[evt.target]['onmessage'].call(global, evt);
        };

        WebSocket.onopen = function (evt) {
            WebSocket.store[evt.target]['onopen'].call(global, evt);
        };

        WebSocket.onclose = function (evt) {
            WebSocket.store[evt.target]['onclose'].call(global, evt);
        };

        WebSocket.onerror = function (evt) {
            WebSocket.store[evt.target]['onerror'].call(global, evt);
        };

        // instance event methods
        WebSocket.prototype.send = function (data) {
            this.socket.send(data);
        };

        WebSocket.prototype.close = function () {
            this.socket.close();
        };

        WebSocket.prototype.getReadyState = function () {
            this.socket.getReadyState();
        };
        ///////////// Must be overloaded
        WebSocket.prototype.onopen = function () {
            throw new Error('onopen not implemented.');
        };

        // alerts message pushed from server
        WebSocket.prototype.onmessage = function (msg) {
            throw new Error('onmessage not implemented.');
        };

        // alerts message pushed from server
        WebSocket.prototype.onerror = function (msg) {
            throw new Error('onerror not implemented.');
        };

        // alert close event
        WebSocket.prototype.onclose = function () {
            throw new Error('onclose not implemented.');
        };
    }
})();