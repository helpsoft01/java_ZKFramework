function clickTab(markerId,x){
	if(x==1){
		document.getElementById(markerId+"_cont1").style.display='inline-block';
		document.getElementById(markerId+"_cont2").style.display='none';
		document.getElementById(markerId+"_cont3").style.display='none';
		document.getElementById(markerId+"_cont4").style.display='none';
		
		$(markerId+"_tab1").addClass("active");
		$(markerId+"_tab2").removeClass("active");
		$(markerId+"_tab3").removeClass("active");
		$(markerId+"_tab4").removeClass("active");
	} else if(x==2){
		document.getElementById(markerId+"_cont1").style.display='none';
		document.getElementById(markerId+"_cont2").style.display='inline-block';
		document.getElementById(markerId+"_cont3").style.display='none';
		document.getElementById(markerId+"_cont4").style.display='none';
		
		$(markerId+"_tab1").removeClass("active");
		$(markerId+"_tab2").addClass("active");
		$(markerId+"_tab3").removeClass("active");
		$(markerId+"_tab4").removeClass("active");
	} else if(x==3){
		document.getElementById(markerId+"_cont1").style.display='none';
		document.getElementById(markerId+"_cont2").style.display='none';
		document.getElementById(markerId+"_cont3").style.display='inline-block';
		document.getElementById(markerId+"_cont4").style.display='none';
		
		$(markerId+"_tab1").removeClass("active");
		$(markerId+"_tab2").removeClass("active");
		$(markerId+"_tab3").addClass("active");
		$(markerId+"_tab4").removeClass("active");
	} else if(x==4){
		document.getElementById(markerId+"_cont1").style.display='none';
		document.getElementById(markerId+"_cont2").style.display='none';
		document.getElementById(markerId+"_cont3").style.display='none';
		document.getElementById(markerId+"_cont4").style.display='inline-block';
		
		$(markerId+"_tab1").removeClass("active");
		$(markerId+"_tab2").removeClass("active");
		$(markerId+"_tab3").removeClass("active");
		$(markerId+"_tab4").addClass("active");
	}
	
}
