/**
 * custom widget for dialog
 */

$.widget("ui.showDialog",{
    options: {
        title: "",
        dialogClass: "",
        url: "",
        data: null,
        type: "",
        action: "",
        width: 200,
        height: 200,
        hideClose: null
    },
    _create: function() {
    	var self = this;
    	var left = (self.options.width-32)/2;
    	var top  = 80;
    	if(self.options.height!="auto"){
        	top  = (self.options.height-32)/2;	
    	}
    	this.loading = $("<img src='/static/images/loading.gif' style='width:32px;height:32px;position:absolute;'>").appendTo(this.element);
    	this.loading.css({"top": top, "left": left});
    	if (self.options.data) {
    		this.element.data("data", self.options.data);
    	}
    	var frameHtml="<iframe allowtransparency='true' id='dialogFrame' name='dialogFrame' src="+self.options.url+"  width="+self.options.width+" height="+self.options.height+" frameborder='0'  scrolling='no' style='background:transparent' />";
    	this.element.append(frameHtml);
    	this.element.bind("closeDialog", function(event, result) {
    		if (result) {
        		if (result.status==1) {
            		self.destroy();
        			if (self.options.action=="create") {
        				$("body").trigger(EVENT_CREATE, [result, self.options.type]);
        			} else {
        				$("body").trigger(EVENT_UPDATE, [result, self.options.type]);
        			}		
	    		} else {			   
	    			$("body").trigger(EVENT_ERROR, [result, self.options.type]);
	    		}
    		} else {
        		self.destroy();
    		}
    	});
    	this.element.bind("refresh", function(event, url) {
    		$("#dialogFrame").attr("src", url);
    	});
    	this.element.bind("loaded", function(event, result) {
    		self.loading.hide();
    	});
    	this.element.bind("resize", function(event, result) {
    		self.element.find("#dialogFrame").height(result);
    		if(self.dialog) {
        		self.dialog.dialog({ position: 'center' });	
    		}
    	});   	
    },
    _init: function() {
    	var self = this;
    	var dialogModal = true;
//    	var overlay = $(".ui-widget-overlay");
//    	if(overlay && overlay.length>0){
//    		dialogModal = false;
//    	}
    	this.dialog = this.element.dialog({
    		title: self.options.title,
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: dialogModal,
			width: self.options.width,
			height: "auto",
			close: function() {
				self.destroy();
			}
		});
    	if(self.options.hideClose){
    		this.element.prev().find(".ui-dialog-titlebar-close").hide();
    	}
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    }
});
$.widget("ui.warningDialog",{
    options: {
    	title: "",
        message: null,
        dialogClass: "",
        actions: [],
        callback: null
    },
    _create: function() {
    	var self = this;
    	this.element.attr("title", self.options.title);
    	this.messageContainer = $("<div style='text-align:center;margin: 20px auto;'></div>").appendTo(this.element);
    	$("<img src='/static/images/warn_bg.jpg' style='margin-right:15px;width:23px;height:26px;'/>").appendTo(this.messageContainer);
    	this.message = $("<span></span>").appendTo(this.messageContainer);
    	this.message.text(this.options.message);
    	var btnContainer = $("<div style='text-align:center'></div>").appendTo(this.element);
    	var OkBtn = $("<input class='form-button' type='button' value='"+self.options.actions[0]+"' />").appendTo(btnContainer);
    	OkBtn.click(function() {
    		self.closeDialog();
    		if(self.options.callback) {
    			self.options.callback();
    		}     	    		
    	});
    	
    },
    _init: function() {
    	var self = this;
    	this.dialog = this.element.dialog({
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: true,
			width: 320,
			height: 160,
			close: function() {
				self.closeDialog();
			}
		});
    	
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    },
    closeDialog: function() {
    	var self = this;
		self.destroy();
    }
});
$.widget("ui.errorDialog",{
    options: {
    	title: "",
        message: null,
        dialogClass: "",
        actions: [],
        callback: null
    },
    _create: function() {
    	var self = this;
    	this.element.attr("title", self.options.title);
    	this.messageContainer = $("<div style='text-align:center;margin: 20px auto;'></div>").appendTo(this.element);
    	$("<img src='/static/images/lose_bg.gif' style='margin-right:15px;width:23px;height:26px;'/>").appendTo(this.messageContainer);
    	this.message = $("<span></span>").appendTo(this.messageContainer);
    	this.message.text(this.options.message);
    	var btnContainer = $("<div style='text-align:center'></div>").appendTo(this.element);
    	var OkBtn = $("<input class='form-button' type='button' value='"+self.options.actions[0]+"' />").appendTo(btnContainer);
    	OkBtn.click(function() {
    		self.closeDialog();
    		if(self.options.callback) {
    			self.options.callback();
    		}     
    	});
    	
    },
    _init: function() {
    	var self = this;
    	this.dialog = this.element.dialog({
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: true,
			width: 320,
			height: 160,
			close: function() {
				self.closeDialog();
			}
		});
    	
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    },
    closeDialog: function() {
    	var self = this;
		self.destroy();	
    }
});

$.widget("ui.successDialog",{
    options: {
    	title: "",
    	dialogClass: "",
        message: null,
        actions: [],
        callback: null
    },
    _create: function() {
    	var self = this;
    	this.element.attr("title", self.options.title);
    	this.messageContainer = $("<div style='text-align:center;margin: 20px auto;'></div>").appendTo(this.element);
    	$("<img src='/static/images/success_bg.jpg' style='margin-right:15px;width:27px;height:26px;'/>").appendTo(this.messageContainer);
    	this.message = $("<span></span>").appendTo(this.messageContainer);
    	this.message.text(this.options.message);
    	var btnContainer = $("<div style='text-align:center'></div>").appendTo(this.element);
    	var OkBtn = $("<input class='form-button' type='button' value='"+self.options.actions[0]+"' />").appendTo(btnContainer);
    	OkBtn.click(function() {
    		self.closeDialog();
    		if(self.options.callback) {
    			self.options.callback();
    		}        		
    	});
    },
    _init: function() {
    	var self = this;
    	this.dialog = this.element.dialog({
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: true,
			width: 320,
			height: 160,
			close: function() {
				self.closeDialog();
			}
		});
    	
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    },
    closeDialog: function() {
    	var self = this;
		self.destroy(); 	
    }
});
$.widget("ui.confirmDialog",{
    options: {
    	title: "",
    	dialogClass: "",
        message: null,
        actions: [],
        callback: null
    },
    _create: function() {
    	var self = this;
    	this.element.attr("title", self.options.title);
    	this.messageContainer = $("<div style='text-align:center;margin: 20px auto;'></div>").appendTo(this.element);
    	$("<img src='/static/images/warn_bg.jpg' style='margin-right:15px;width:30px;height:26px;'/>").appendTo(this.messageContainer);
    	this.message = $("<span></span>").appendTo(this.messageContainer);
    	this.message.text(this.options.message);
    	var btnContainer = $("<div style='text-align:center'></div>").appendTo(this.element);
    	var OkBtn = $("<input class='form-button' type='button' value='"+self.options.actions[0]+"' />").appendTo(btnContainer);
    	OkBtn.click(function() {
    		self.closeDialog();
    		if(self.options.callback) {
    			self.options.callback();
    		}      		
    	});
    	var CancelBtn = $("<input class='form-button' style='margin-left: 50px;' type='button' value='"+self.options.actions[1]+"' />").appendTo(btnContainer);
    	CancelBtn.click(function() {
    		self.destroy();
    	});
    },
    _init: function() {
    	var self = this;
    	this.dialog = this.element.dialog({
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: true,
			width: 320,
			height: 160,
			close: function() {
				self.closeDialog();
			}
		});
    	
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    },
    closeDialog: function() {
    	var self = this;
		self.destroy();   	
    }
});
$.widget("ui.alertDialog",{
    options: {
    	title: "",
    	dialogClass: "",
    	type: "success", //success, error, confirm
        message: null,
        actions: [],
        callback: null
    },
    _create: function() {
    	var self = this;
    	var table = $('<table class="overlay-panel" cellspacing="0" cellpadding="0" border="0"></table>').appendTo(this.element);
    	//header
    	var hearTR = $('<tr class="no-header"></tr>').appendTo(table);
    	$('<td class="overlay-hdL"></td>').appendTo(hearTR);
    	$('<td class="overlay-hdC"></td>').appendTo(hearTR);
    	$('<td class="overlay-hdR"></td>').appendTo(hearTR);
    	//center
    	var centerTR = $('<tr></tr>').appendTo(table);
    	$('<td class="overlay-bdL"></td>').appendTo(centerTR);
    	var centerTD = $('<td class="overlay-content" style="width:450px;"></td>').appendTo(centerTR);
    	var msgTop = $('<div class="prompt_message_top"></div>').appendTo(centerTD);
    	var closeLink = $('<a href="javascript:;"></a>').appendTo(msgTop);
    	closeLink.click(function() {
    		self.closeDialog();
    	});
    	$('<h3>提示</h3>').appendTo(msgTop);
    	$('<img class="toa" width="410" height="1" src="/static/images/min.jpg">').appendTo(centerTD);
    	var msgMain = $('<div class="prompt_message_main" style="background-color:#FFFFFF;"></div>').appendTo(centerTD);
    	var dl = $('<dl></dl>').appendTo(msgMain);
    	var dt = $('<dt></dt>').appendTo(dl);
    	if(self.options.type=="success"){
    		$('<img width="33" height="26" src="/static/images/add_messages.png">').appendTo(dt);
    	} else if(self.options.type=="error") {
    		$('<img width="26" height="26" src="/static/images/add_messages02.png">').appendTo(dt);
    	} else if(self.options.type=="confirm") {
    		$('<img width="31" height="28" src="/static/images/add_messages03.png">').appendTo(dt);
    	}
    	var dd = $('<dd></dd>').appendTo(dl);
    	var msg = $('<p>操作成功！</p>').appendTo(dd);
    	msg.text(self.options.message);
    	if(self.options.type=="success" || self.options.type=="error"){
    		this.okButton = $('<a class="prompt_message_btn03" href="javascript:;"></a>').appendTo(centerTD);
    		$('<img width="16" height="14" align="absmiddle" style=" padding-right:5px;" src="/static/images/ys_r_bg.png"></img>').appendTo(this.okButton);
    		var okText = $('<span></span>').appendTo(this.okButton);
    		okText.text(self.options.actions[0]);
    	} else if(self.options.type=="confirm") {
    		this.cancelButton = $('<a class="prompt_message_btn01" href="javascript:;"></a>').appendTo(centerTD);
    		$('<img width="14" height="14" align="absmiddle" style=" padding-right:5px;" src="/static/images/quxiao.png">').appendTo(this.cancelButton);
    		var cancelText = $('<span></span>').appendTo(this.cancelButton);
    		cancelText.text(self.options.actions[0]);
    		this.okButton = $('<a class="prompt_message_btn02" href="javascript:;"></a>').appendTo(centerTD);
    		$('<img width="16" height="14" align="absmiddle" style=" padding-right:5px;" src="/static/images/ys_r_bg.png">').appendTo(this.okButton);
    		var okText = $('<span></span>').appendTo(this.okButton);
    		okText.text(self.options.actions[1]); 
    	}
    	$('<td class="overlay-bdR"></td>').appendTo(centerTR);
    	//footer
    	var footerTR = $('<tr></tr>').appendTo(table);
    	$('<td class="overlay-ftL"></td>').appendTo(footerTR);
    	$('<td class="overlay-ftC"></td>').appendTo(footerTR);
    	$('<td class="overlay-ftR"></td>').appendTo(footerTR);
    	this.okButton.click(function() {
    		self.closeDialog();
    		if(self.options.callback) {
    			self.options.callback();
    		}
    	});
    	if(this.cancelButton){
        	this.cancelButton.click(function() {
        		self.closeDialog();
        	});	
    	}
    },
    _init: function() {
    	var self = this;
    	this.dialog = this.element.dialog({
    		dialogClass: self.options.dialogClass,
			autoOpen: true,
			resizable: false,
			modal: true,
			minWidth: 474,
			width: 474,
			height: "auto",
			close: function() {
				self.closeDialog();
			}
		});
    	
    },
    destroy: function() {
        $.Widget.prototype.destroy.apply(this, arguments);
        this.element.empty().remove();
    },
    closeDialog: function() {
    	var self = this;
		self.destroy();     	
    }
});