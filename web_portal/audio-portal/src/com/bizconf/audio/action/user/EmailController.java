package com.bizconf.audio.action.user;

import com.libernate.liberc.ActionForward;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;
import com.libernate.liberc.annotation.httpmethod.Get;

@ReqPath("email")
public class EmailController {
	
	
	@AsController(path = "outlook")
	@Get
	public Object emailOutLook() {
		return new ActionForward.Forward("/jsp/user/add_calendar_notice.jsp");
	}
}
