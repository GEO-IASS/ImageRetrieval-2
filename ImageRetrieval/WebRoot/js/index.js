// JavaScript Document
//直接加载:hover 图片放大过度不流畅
$(document).ready(function(){
	$("#zoom img").hover(function(){$(this).addClass("hover")},
		function(){$(this).removeClass("hover")}
	)	
})
