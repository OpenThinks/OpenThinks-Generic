/**
 * OpenThinks utility library.
 *
 * @author Zhang Junlong
 *
 * Copyright 2008-2012, OpenThinks.org
 */
var ot = ot ? ot : {};
ot.utils = ot.utils ? ot.utils : new OTUtils();
ot.utils.object = ot.utils.object ? ot.utils.object : new OTObjectUtil();
ot.support = ot.support ? ot.support : new OTBrowserDetector();
ot.offline = ot.offline ? ot.offline : new OTOfflineHelper();
ot.utils.url = ot.utils.url ? ot.utils.url : new OTUrlUtil();

/**
 * Browser Detector
 */
function OTBrowserDetector() {
    this._osInfo = {osName: 'undetected', mobile: false};
    this._capInfo = {};
}
/**
 * Get operation system information.
 *
 * @returns {Boolean}
 */
OTBrowserDetector.prototype.getOSInfo = function () {
    if (this._osInfo.osName != 'undetected') {
        return this._osInfo;
    }

    var uaString = navigator.userAgent.toLowerCase();
    if (uaString.match(/ipad/i)) {
        this._osInfo.osName = 'iOS for iPad';
        this._osInfo.mobile = true;
    } else if (uaString.match(/iphone/i)) {
        this._osInfo.osName = 'iOS for iPhone';
        this._osInfo.mobile = true;
    } else if (uaString.match(/mac os/i)) {
        this._osInfo.osName = 'Mac OS X';
    } else if (uaString.match(/android/i)) {
        this._osInfo.osName = 'Android';
        this._osInfo.mobile = true;
    } else if (uaString.match(/windows nt/i)) {
        this._osInfo.osName = 'Windows';
    } else if (uaString.match(/windows phone/i)) {
        this._osInfo.osName = 'Windows Phone';
        this._osInfo.mobile = true;
    } else if (uaString.match(/windows mobile/i)) {
        this._osInfo.osName = 'Windows Mobile';
        this._osInfo.mobile = true;
    } else if (uaString.match(/windows ce/i)) {
        this._osInfo.osName = 'Windows CE';
        this._osInfo.mobile = true;
    } else if (uaString.match(/midp/i)) {
        this._osInfo.osName = 'MID';
        this._osInfo.mobile = true;
    } else if (uaString.match(/rv:1.2.3.4/i)) {
        this._osInfo.osName = 'UCWeb7';
        this._osInfo.mobile = true;
    } else if (uaString.match(/ucweb/i)) {
        this._osInfo.osName = 'UCWeb';
        this._osInfo.mobile = true;
    }

    return this._osInfo;
};
OTBrowserDetector.prototype.getCapInfo = function () {
    var _this = this;
    window.WebSocket = window.WebSocket || window.MozWebSocket;
    this._capInfo.webSocket = !!window.WebSocket;
    this._capInfo.webWorkers = !!window.Worker;
    this._capInfo.geoLocation = !!navigator.geolocation;
    this._capInfo.database = !!window.openDatabase;
    this._capInfo.sessionStorage = !!window.sessionStorage;
    this._capInfo.localStorage = !!window.localStorage;
    var videoElement = document.createElement("video");
    this._capInfo.video = this.video = !!videoElement["canPlayType"];
    var ogg = false;
    var h264 = false;
    if (this._capInfo.video) {
        ogg = videoElement.canPlayType('video/ogg; codecs="theora, vorbis"')
            || "no";
        h264 = videoElement
            .canPlayType('video/mp4; codecs="avc1.42E01E, mp4a.40.2"')
            || "no";
    }

    if (this._capInfo.geoLocation) {
        navigator.geolocation.getCurrentPosition(function (location) {
            _this._capInfo.geoLocation = {
                'latitude': location.coords.latitude,
                'longitude': location.coords.longitude
            };
        });
    }

    if (this._capInfo.video) {
        if (h264) {
            this._capInfo.video = ' H.264 ';
        }
        if (ogg) {
            this._capInfo.video += ' OGG ';
        }
    }

    if (ot.utils.debug) {
        console.log(this._capInfo);
    }

    return this._capInfo;
};

function OTOfflineHelper() {
    this._online = navigator.onLine;
}
OTOfflineHelper.prototype.isOnline = function () {
    return this._online;
};
/**
 * Refresh cached files
 *
 * @param callback
 */
OTOfflineHelper.prototype.refreshCache = function (callback) {
    var refreshed = false;
    var msg = '无更新';
    var appCache = window.applicationCache;

    try {
        appCache.update();
    } catch (e) {
        msg = '更新出错';
    }

    if (appCache.status == window.applicationCache.UPDATEREADY) {
        appCache.swapCache();
        msg = '更新完成';
        refreshed = true;
    }

    if (callback) {
        callback(refreshed, msg);
    }
};
/**
 * Add an event to window.applicationCache.
 *
 * Supported events:onchecking, onerror, onnoupdate, ondownloading, onprogress,
 * onupdateready, oncached and onobsolete.
 *
 * @param event
 * @param handleCallback
 */
OTOfflineHelper.prototype.addCacheListener = function (event, handleCallback) {
    window.applicationCache.addEventListener(event, handleCallback, false);
};
OTOfflineHelper.prototype.addUpdateListeners = function (callback) {
    ot.offline.addCacheListener('checking', function (e) {
        callback('checking', '检查程序更新...', e);
    });
    ot.offline.addCacheListener('downloading', function (e) {
        callback('downloading', '开始下载程序更新...', e);
    });
    ot.offline.addCacheListener('progress', function (e) {
        callback('progress', '正在更新程序...', e);
    });
    ot.offline.addCacheListener('cached', function (e) {
        callback('cached', '程序(全新)更新完成.', e);
    });
    ot.offline.addCacheListener('updateready', function (e) {
        window.applicationCache.swapCache();
        callback('updateready', '程序更新完成.', e);
    });
    ot.offline.addCacheListener('noupdate', function (e) {
        callback('noupdate', '程序无更新.', e);
    });
    ot.offline.addCacheListener('obsolete', function (e) {
        callback('obsolete', '服务器程序文件异常.', e);
    });
    ot.offline.addCacheListener('error', function (e) {
        callback('error', '更新过程出现错误，更新中止.', e);
    });
};
OTOfflineHelper.prototype.addStatusListener = function (callback) {
    document.body.addEventListener("online", function () {
        applicationCache.update();
        applicationCache.addEventListener("updateready", function () {
            applicationCache.swapCache();

        }, false);

        if (callback) {
            callback(navigator.onLine);
        }
    }, false);
    document.body.addEventListener("offline", function () {
        $("#olStatus").css('block si-ui sprite-cloud');

        if (callback) {
            callback(navigator.onLine);
        }
    }, false);
};

function OTWebSocketHelper() {
    this.receiver = null; // callback function when notification received
    this.socket = null;
    this._disconnectHandle = null;
}
OTWebSocketHelper.prototype.connect = function (url) {
    var _this = this;
    if (url.indexOf('ws') != 0) {
        var slash = '/';
        if (url.indexOf('/') == 0)
            slash = '';
        if (window.location.protocol == 'http:') {
            url = 'ws://' + window.location.host + slash + url;
        } else {
            url = 'wss://' + window.location.host + slash + url;
        }
    }

    if ('WebSocket' in window) {
        _this.socket = new WebSocket(url);
    } else if ('MozWebSocket' in window) {
        _this.socket = new MozWebSocket(url);
    } else {
        if (ot.utils.debug) {
            console.error('WebSocket is not supported by this browser.');
        } else {
            alert('系统不支持WebSocket');
        }
        return;
    }

    _this.socket.onopen = function () {
        if (ot.utils.debug) {
            console.log('WebSocket opened at ' + url);
        }
    };
};
OTWebSocketHelper.prototype.send = function (message) {
    if (message != '') {
        this.socket.send(message);
    }
};
OTWebSocketHelper.prototype.setReceiver = function (callback) {
    this.socket.onmessage = function (event) {
        if (callback) {
            callback(event.data);
        }
    };
};
OTWebSocketHelper.prototype.setDisconnectHandler = function (callback) {
    this.socket.onclose = function () {
        if (ot.utils.debug)
            console.log('WebSocket closed.');

        if (callback) {
            callback();
        }
    }
};

function OTUrlUtil() {
    this.getParameter = function (paramName) {
        var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r)
            return decodeURIComponent(r[2]);
        else
            return null;
    };
}

/**
 * HashMap
 *
 */
function HashMap() {
    var size = 0;
    var entry = {};

    this.put = function (key, value) {
        if (!this.containsKey(key)) {
            size++;
        }
        entry[key] = value;
    };

    this.get = function (key) {
        return this.containsKey(key) ? entry[key] : null;
    };

    this.remove = function (key) {
        if (this.containsKey(key) && (delete entry[key])) {
            size--;
        }
    };

    this.containsKey = function (key) {
        return (key in entry);
    };

    this.containsValue = function (value) {
        for (var prop in entry) {
            if (entry[prop] == value) {
                return true;
            }
        }
        return false;
    };

    this.values = function () {
        var values = [];
        for (var prop in entry) {
            values.push(entry[prop]);
        }
        return values;
    };

    this.size = function () {
        return size;
    };

    this.keys = function () {
        var keys = [];
        for (var prop in entry) {
            keys.push(prop);
        }
        return keys;
    };

    this.clear = function () {
        size = 0;
        entry = {};
    };
}

function OTUtils() {

}
OTUtils.prototype.round = function (number, bit) {
    return Math.round(number * Math.pow(10, bit)) / Math.pow(10, bit);
};
/**
 * Convert a array of grouped objects to a table like 2D array data structure
 */
OTUtils.prototype.convertToTable = function (items, keysOfXAxis, keysOfYAxis, keyOfVal) {
    var keyOfXAxisId = null, keyOfXAxisName = null, keyOfYAxisId = null, keyOfYAxisName = null;
    var xAxisIndex = {count: 0};
    var yAxisIndex = {count: 0};
    var dyadicDataArray = [];

    if (keysOfXAxis instanceof Array) {
        keyOfXAxisId = keysOfXAxis[0];
        keyOfXAxisName = keysOfXAxis[1];
    } else {
        keyOfXAxisId = keysOfXAxis;
    }

    if (keysOfYAxis instanceof Array) {
        keyOfYAxisId = keysOfYAxis[0];
        keyOfYAxisName = keysOfYAxis[1];
    } else {
        keyOfYAxisId = keysOfYAxis;
    }
    for (var i = 0; i < items.length; i++) {
        var valOfXAxis4Id = eval('item[i].' + keyOfXAxisId);
        var valOfXAxis4Name = eval('item[i].' + keyOfXAxisName);
        var valOfyAxis4Id = eval('item[i].' + keyOfYAxisId);
        var valOfyAxis4Name = eval('item[i].' + keyOfYAxisName);
        var valOfv = eval('item[i].' + keyOfVal);
        var xKey = 'x_' + valOfXAxis4Id;
        var xIndex;

        if (null == xAxisIndex[xKey]) {
            xAxisIndex[xKey] = [xAxisIndex.count, valOfXAxis4Name];
            xAxisIndex.count++;
        }
        xIndex = xAxisIndex[xKey][0];

        var yKey = 'y_' + valOfyAxis4Id;
        var yIndex;
        if (null == yAxisIndex[yKey]) {
            yAxisIndex[yKey] = [yAxisIndex.count, valOfyAxis4Name];
            yAxisIndex.count++;
        }
        yIndex = yAxisIndex[yKey][0];

        var yDataArray = dyadicDataArray[xIndex];
        if (null == yDataArray) {
            dyadicDataArray[xIndex] = [];
        }
        dyadicDataArray[xIndex][yIndex] = valOfv;
    }

    return {
        xAxisIndex: xAxisIndex,
        yAxisIndex: yAxisIndex,
        dataArray: dyadicDataArray
    };
};
OTUtils.prototype.convertToComboTable = function (items, keyOfAxis, keyOfAxisId, keyOfAxisName, xAxisMap) {
    var complexObj = false;
    var comboX = false;
    if (keyOfAxisId)
        complexObj = true;

    if (xAxisMap)
        comboX = true;

    var xAxisIndex = {count: 0};
    var yAxisIndex = {count: 0};
    var dyadicDataArray = [];

    $.each(items, function (ind, item) {
        var valOfYAxis4Id = 'x_';
        var len = item[keyOfAxis].length;
        for (var i = 0; i < len - 1; i++) {
            if (complexObj) {
                valOfYAxis4Id += item[keyOfAxis][i][keyOfAxisId];
            } else {
                valOfYAxis4Id += item[keyOfAxis][i];
            }
        }

        var valOfYAxis4Name = item[keyOfAxis][len - 2];
        var valOfXAxis4Id = item[keyOfAxis][len - 1];
        var valOfXAxis4Name = item[keyOfAxis][len - 1];

        if (complexObj) {
            valOfYAxis4Name = valOfYAxis4Name[keyOfAxisName];
            valOfXAxis4Id = valOfXAxis4Id[keyOfAxisId];
            valOfXAxis4Name = valOfXAxis4Name[keyOfAxisName];
        }

        if (comboX) {
            valOfYAxis4Id += xAxisMap[item[keyOfAxis][len - 1]][0];
            valOfXAxis4Id = xAxisMap[item[keyOfAxis][len - 1]][1];
        }

        var xIndex;
        if (null == xAxisIndex['xId' + valOfXAxis4Id]) {
            xAxisIndex['xId' + valOfXAxis4Id] = [xAxisIndex.count, valOfXAxis4Name];
            xAxisIndex.count++;
        } else {
            if (!xAxisIndex['xId' + valOfXAxis4Id][1].contains(valOfXAxis4Name)) {
                xAxisIndex['xId' + valOfXAxis4Id][1] += '/';
                xAxisIndex['xId' + valOfXAxis4Id][1] += valOfXAxis4Name;
            }
        }
        xIndex = xAxisIndex['xId' + valOfXAxis4Id][0];

        var yKey = 'y_' + valOfYAxis4Id;
        var yIndex;
        if (null == yAxisIndex[yKey]) {
            yAxisIndex[yKey] = [yAxisIndex.count, valOfYAxis4Name];
            yAxisIndex.count++;
        }
        yIndex = yAxisIndex[yKey][0];

        var yDataArray = dyadicDataArray[xIndex];
        if (null == yDataArray) {
            dyadicDataArray[xIndex] = [];
        }
        dyadicDataArray[xIndex][yIndex] = item;
    });
    //Sorting key on x axis
    if (xAxisIndex.count > 1) {
        var xAxisIndexKeyArray = [];
        var sortedXAxisIndexKeyArray = [];
        for (var key in xAxisIndex) {
            if (key != 'count') {
                xAxisIndexKeyArray.push(key);
                sortedXAxisIndexKeyArray.push(key);
            }
        }
        sortedXAxisIndexKeyArray.sort();

        for (var i = 0; i < xAxisIndexKeyArray.length; i++) {
            if (xAxisIndexKeyArray[i] > sortedXAxisIndexKeyArray[i]) {
                var tmp = xAxisIndex[xAxisIndexKeyArray[i]];
                xAxisIndex[xAxisIndexKeyArray[i]] = xAxisIndex[sortedXAxisIndexKeyArray[i]];
                xAxisIndex[sortedXAxisIndexKeyArray[i]] = tmp;

                tmp = dyadicDataArray[xAxisIndex[xAxisIndexKeyArray[i]][0]];
                dyadicDataArray[xAxisIndex[xAxisIndexKeyArray[i]][0]] = dyadicDataArray[xAxisIndex[sortedXAxisIndexKeyArray[i]][0]];
                dyadicDataArray[xAxisIndex[sortedXAxisIndexKeyArray[i]][0]] = tmp;
            }
        }
    }

    return {
        xAxisIndex: xAxisIndex,
        yAxisIndex: yAxisIndex,
        dataArray: dyadicDataArray
    };
};
OTUtils.prototype.createCompleteChecker = function (totalCnt, callback) {
    return new function () {
        var cnt = 0;
        var tCnt = 0;
        if (null == totalCnt)
            tCnt = 0;
        else
            tCnt = totalCnt;
        this.check = function () {
            cnt++;
            if (cnt == tCnt) {
                if (callback)
                    callback();
            }
        }
        this.end = function () {
            cnt = tCnt - 1;
            this.check();
        }
    };
};

function OTObjectUtil() {

}
/**
 * Traverse all properties of specified object, and display them by using
 * alert()
 *
 * @param object
 * @param property name of traversing object ,this could be used for nesting object
 * @param callback callback for traversed property and value
 */
OTObjectUtil.prototype.traverse = function (object, property, callback) {
    var prop = '';
    if (property) {
        prop = property + '.';
    }
    for (var key in object) {
        var value = object[key];
        if ((typeof object[key]) == "function") {
            object[key]('Traverse Test.');
        } else {
            if (typeof object[key] == "object") {
                this.traverse(object[key], prop + key, callback);
            } else {
                if (callback) {
                    callback(prop + key, value);
                } else {
                    console.log(prop + key + ': ' + value);
                }
            }
        }
    }
};
OTObjectUtil.prototype.hierarchize = function (obj) {
    var hObj = {};
    for (var key in obj) {
        if (key.contains('_')) {
            var i = key.split('_');
            hObj[i[0]] = typeof(hObj[i[0]]) == 'object' ? hObj[i[0]] : {};
            hObj[i[0]][i[1]] = obj[key];
        } else {
            hObj[key] = obj[key];
        }
    }
    return hObj;
};
OTObjectUtil.prototype.getType = function (obj) {
    var _t;
    return ((_t = typeof(obj)) == "object" ? obj == null && "null" || Object.prototype.toString.call(obj).slice(8, -1) : _t).toLowerCase();
};
OTObjectUtil.prototype.isString = function (obj) {
    return this.getType(obj) == "string";
};
OTObjectUtil.prototype.isArray = function (obj) {
    return Object.prototype.toString.call(obj) === '[object Array]';
};
/**
 * Extends String
 */
String.prototype.test = function (regex, params) {
    return ((typeof(regex) == 'regexp') ? regex
        : new RegExp('' + regex, params)).test(this);
};
String.prototype.contains = function (string, separator) {
    return (separator) ? (separator + this + separator).indexOf(separator
        + string + separator) > -1 : String(this).indexOf(string) > -1;
};

String.prototype.trim = function () {
    return String(this).replace(/^\s+|\s+$/g, '');
};

String.prototype.clean = function () {
    return String(this).replace(/\s+/g, ' ').trim();
};

String.prototype.toCamelCase = function () {
    return String(this).replace(/-\D/g, function (match) {
        return match.charAt(1).toUpperCase();
    });
};
String.prototype.hyphenate = function () {
    return String(this).replace(/[A-Z]/g,function (match) {
        return ('-' + match.charAt(0).toLowerCase());
    }).replace(/^-/g, '');


};
String.prototype.capitalize = function () {
    return String(this).replace(/\b[a-z]/g, function (match) {
        return match.toUpperCase();
    });
};
String.prototype.escapeRegExp = function () {
    return String(this).replace(/([-.*+?^${}()|[\]\/\\])/g, '\\$1');
};
String.prototype.toInt = function (base) {
    return parseInt(this, base || 10);
};
String.prototype.toFloat = function () {
    return parseFloat(this);
};
String.prototype.hexToRgb = function (array) {
    var hex = String(this).match(/^#?(\w{1,2})(\w{1,2})(\w{1,2})$/);
    return (hex) ? hex.slice(1).hexToRgb(array) : null;
};
String.prototype.rgbToHex = function (array) {
    var rgb = String(this).match(/\d{1,3}/g);
    return (rgb) ? rgb.rgbToHex(array) : null;
};
String.prototype.substitute = function (object, regexp) {
    return String(this).replace(regexp || (/\\?\{([^{}]+)\}/g),
        function (match, name) {
            if (match.charAt(0) == '\\')
                return match.slice(1);
            return (object[name] != null) ? object[name] : '';
        });
};
String.prototype.isValidEmail = function () {
    var str = String(this);
    if ((str == null) || (str.length < 2))
        return false;
    var aPos = str.indexOf("@", 1);
    if (aPos < 0) {
        return false;
    }

    if (str.indexOf(".", aPos + 2) < 0) {
        return false;
    }

    var s = str.charAt(str.length - 1);
    return ('a' <= s && s <= 'z') || ('A' <= s && s <= 'Z');

};
String.prototype.isDate = function () {
    var dateStr = String(this);
    var parts;

    if (dateStr.indexOf("-") > -1) {
        parts = dateStr.split('-');
    } else if (dateStr.indexOf("/") > -1) {
        parts = dateStr.split('/');
    } else {
        return false;
    }

    if (parts.length < 3) {
        //Integrity checking
        return false;
    }

    for (var i = 0; i < 3; i++) {
        if (!isNaN(parts[i])) {
            return false;
        }
    }

    var y = parts[0];//Year
    var m = parts[1];//Month
    var d = parts[2];//Date

    if (y > 3000) {
        return false;
    }

    if (m < 1 || m > 12) {
        return false;
    }

    switch (d) {
        case 29:
            if (m == 2) {
                //Special for leap year
                if ((y / 100) * 100 == y && (y / 400) * 400 != y) {
                    //
                } else {
                    return false;
                }
            }
            break;
        case 30:
            if (m == 2) {
                //Special check for February
                return false;
            }
            break;
        case 31:
            if (m == 2 || m == 4 || m == 6 || m == 9 || m == 11) {
                //Special for month with 30 days
                return false;
            }
            break;
        default:

    }
    return true;
};