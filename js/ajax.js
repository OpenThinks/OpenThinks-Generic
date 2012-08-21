<!--
/**
  *Initiating XMLHttpRequest in defferent browsers
  */
function createXMLHttp() {

	var request = false;
	
	try {
		request = new XMLHttpRequest();
	} catch (trymicrosoft) {
		try {
			request = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (othermicrosoft) {
			try {
				request = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (failed) {
				request = false;
			}
		}
	}
	
	if (!request)
		alert(" Failed to Initialize XMLHttpRequest!");
	
	return request;
}

/**
 *Load a resource by specified url and display it in specifed target
 */
function loadUrl(url, displayTarget, waitPhrase) {
 	var request=false;
	
	if (!document.getElementById) return false;
 	
	if (waitPhrase != null) document.getElementById(displayTarget).innerHTML = waitPhrase;
	
	request = createXMLHttp();

 	request.onreadystatechange = function() {
		if (request.readyState < 4) {
			document.getElementById(displayTarget).innerHTML = "<img src='images/loading.gif' alt='loading...'><span style='padding-left:6px; font-size: 14px;';>Please Wait...</span>";
		} else {
			document.getElementById(displayTarget).innerHTML = (request.status == 200) ? request.responseText : "Sorry! You requested resource does not exist.<br>Error: status code is  " + request.status;
		}
 	}
 	
 	request.open("GET", url, true);
 	request.send(null);
}

function urlLoading(request, url, displayTarget) {
 	//alert(request.readyState + ' -- ' + displayTarget);
	//alert(request.readyState);
	if (request.readyState < 4) {
		document.getElementById(displayTarget).innerHTML = "<img src='images/loading.gif' alt='loading...'><span style='padding-left:6px; font-size: 14px; color:#FF9900';>Please Wait...</span>";
	} else {
		document.getElementById(displayTarget).innerHTML = (request.status == 200) ? request.responseText : "Sorry! A broken request! Error codes is: " + request.status;
	}
}
//-->