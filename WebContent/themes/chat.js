/**
 * author Viet Ha Ca
 */
$(document).ready(function() {
	setVboardWidth();
});
function setVboardWidth(){
	var width=jq('$divBoard').width();
	jq('$vbBoard').css('width', width);
	jq('$vbBoard').css('max-width', width);
	height=jq('$divBoard').height();
	jq('$vbBoard').css('height', height);
}

function toggleCss() {
	$('.divChat').toggleClass('divChat1');
}
