$(document).ready(function(){
	/*  ADDING CSRF TOKEN FROM META TO ALL AJAX REQUEST */
	var header = $("meta[name=_csrf_header]").attr("content");
	var token =  $("meta[name=_csrf]").attr("content");
	$.ajaxSetup({
		beforeSend: function(xhr, settings) {
	            xhr.setRequestHeader(header, token);
	    }
    });
});