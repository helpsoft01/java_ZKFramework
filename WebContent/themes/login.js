/**
 * @author Viet Ha Ca
 */
var changeLand=false;
var changePor=false;
$(document).ready(function(){
	window.onresize=function() {
	    initMainDiv();
	};
});
function initMainDiv() {
	var marginLeft = (jq("$main_div").width()-jq("$login_content").width())/2;
	var marginTop=(jq("$main_div").height()-jq("$login_content").height())/2;
	jq("$login_content").css("margin-top",marginTop);
	jq("$login_content").css("margin-left",marginLeft);
	
	jq("$imgLogin").css("width",jq("$login_content").width());
	jq("$imgLogin").css("height",jq("$login_content").height());
	
// LOGO DANG NHAP
	
	var height=jq("$login_content").height()*0.1;
	jq("$logoPanel").css("height",height);
	marginLeft=jq("$logoPanel").width()*0.01;
	jq("$logoPanel").css("margin-left",marginLeft);
	
	// Tile text margin left
	var titleMarginLeft = (jq("$logoPanel").width()-jq("$titleText").width())/2;
	jq("$titleText").css("margin-left",titleMarginLeft);
	marginTop=(jq("$logoPanel").height()-jq("$titleText").height())/2;
	jq("$titleText").css("margin-top",marginTop);
	
	var width=jq("$login_content").width()-20;
	marginLeft = (jq("$login_content").width()-width)/2;
	height=jq("$login_content").height()*0.6;

	jq("$inputInfo").css("height",height);
	jq("$inputInfo").css("width",width);
	jq("$inputInfo").css("margin-top",height/6);
	jq("$inputInfo").css("margin-left",marginLeft);
	
	marginLeft=	(jq("$vboxInput").width()-jq("$hUser").width())/2;
	jq("$hUser").css("margin-left",marginLeft);
	jq("$hUser").css("margin-top",jq("$hUser").height()/2);
	jq("$hPass").css("margin-left",marginLeft);
	jq("$hPass").css("margin-top",jq("$hPass").height()/2);
	
	width = (jq("$hUser").width() - jq("$logoUser").width());
	jq("$txtUser").css("width",width);
	jq("$txtPass").css("width",width);
	
	//Margin logo user and pass
    resizeLogoHeight=jq("$txtUser").width();//lay lai kich thuoc text
    resize = jq('$logoUser').width();//lay kich thuoc anh
    marginLeft=(jq("$hUser").width()-resizeLogoHeight-resize-10)/2;
    jq('$logoUser').css('margin-left', marginLeft);
    jq('$logoPass').css('margin-left', marginLeft);
    
    //Login button
    marginLeft=(jq("$login_content").width()-jq("$btnLogin").width())/2;
    jq("$btnLogin").css("margin-left",marginLeft);
    var topHeight=jq("$inputInfo").height()+jq("$logoPanel").height();
    var contentHeight= jq("$login_content").width()-topHeight;
    var marginContent=(contentHeight-jq("$btnLogin").height())/2;
    marginTop=topHeight+marginContent;
    jq("$btnLogin").css("margin-top",topHeight);
    
    //focus username
//    jq("$txtUser").focus();
    
    if(jq('$main_div').width()> jq('$main_div').height()){
    	if(changePor==false){
    		jq('$main_div').removeClass("main_landscape");
    		jq('$main_div').addClass("main_div");
    		console.log("change complete!");
    		changePor=true;
    		changeLand=false;
    	}
    }
}
