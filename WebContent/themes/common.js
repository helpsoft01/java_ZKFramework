/**
 * @author habv
 */
$(document).ready(function(){
	window.onresize=function() {
		collapseMenu();
	};
});
function initLogin(){
	var width=jq('$login_hlUser').width()-30;
	jq('$tb_user').css('width',width);
	jq('$tb_pass').css('width',width);
}
function initNavbar(){
	var width=0;
	var i=0;
	var id="nb_item"+i;
	while(true){
		id="nb_item"+i;
		if(jq('$'+id).width()==null){
			break;
		} else {
			width+=jq('$'+id).width();
			i++;
			id="nb_item"+i;
		}
	}
	jq('$nb_menu').css('width',(width+10));

	collapseMenu();
//	width=jq('$nb_menu').width()+jq("$hl_setting").width()+95;
//	console.log(width +" vs "+$(window).width());
//	if(width>$(window).width()){
//		jq('$nb_menu').css('display', 'none');
//		$(jq('$bt_menu')).parent().css('display', 'inline-block');
//		jq('$bt_menu').css('display', 'block');
//	}
//	else {
//		jq('$nb_menu').css('display', 'block');
//		jq('$bt_menu').css('display', 'none');
//		jq('$div_mnu_collapse').css('display', 'none');
//	}
}
function collapseMenu(){
	var width=jq('$nb_menu').width()+jq("$hl_setting").width()+95;
	if(width > jq('.navbar').width()){
		jq('$nb_menu').css('display', 'none');
		$(jq('$bt_menu')).parent().css('display', 'inline-block');
		jq('$bt_menu').css('display', 'block');
	}
	else {
		jq('$nb_menu').css('display', 'block');
		jq('$bt_menu').css('display', 'none');
		jq('$div_mnu_collapse').css('display', 'none');
	}
}
function fixUserLocation(){
	var width=jq('$hl_address_info').width()-30;
	jq('$cb_home_info').css('width',width);
	jq('$cb_comp_info').css('width',width);
	jq('$cb_usually_info').css('width',width);
	var marginLeft=(jq('$hl_address_info').width()-jq('$btn_add_info').width())/2;
	jq('$btn_add_info').css('margin-left', marginLeft);
}

function fixDriverAppTrackings(){
	var height = jq('$div_app_tracking').height() - jq('$comp_div_app_tracking').height();
	jq('$list_app_tracking').css('height',height);
}

function fixListbooxTracking(type){
	//jq('$listbox_vehicle_tracking').height();vehicle_tracking_vlayout_searchdiv
	if(type==0){
		jq('$listbox_vehicle_tracking').css('height', 255);
	} else {
		jq('$listbox_vehicle_tracking').css('height', 320);
	}
}

function initMap() {
	isLoadGoogleMapLibraries = true;
}
