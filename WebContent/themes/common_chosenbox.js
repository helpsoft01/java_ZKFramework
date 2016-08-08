function chosenboxSetFocus(_this, _value) {
	// alert(_this);
	// zk.Widget.$(jq("'$" + _this + "-inp'")).$n().fire('onClick');
	// $("'#" + _this + "-inp'").focus();
	var obj = $("#" + _this + "-inp");
	obj.val(_value);
	obj.trigger("click");

	// obj.focus();

}
