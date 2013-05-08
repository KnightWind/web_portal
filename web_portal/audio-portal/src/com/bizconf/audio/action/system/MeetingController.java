package com.bizconf.audio.action.system;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bizconf.audio.action.BaseController;
import com.bizconf.audio.entity.ConfBase;
import com.bizconf.audio.entity.PageModel;
import com.bizconf.audio.service.ConfService;
import com.libernate.liberc.ActionForward;
import com.libernate.liberc.LiberInvocation;
import com.libernate.liberc.annotation.AsController;
import com.libernate.liberc.annotation.ReqPath;

@ReqPath("meeting")
public class MeetingController extends BaseController {
	
	@Autowired
	ConfService confService;
	
	@AsController(path = "list")
	public Object list(LiberInvocation inv, PageModel pageModel) {
//		List<ConfBase> confList = confService.getConfListBySubject(null,null, null, null, pageModel,null);
		List<ConfBase> confList = null;
		pageModel.setRowsCount(100);
		inv.getRequest().setAttribute("confList", confList);
		return new ActionForward.Forward("/jsp/system/meeting_list.jsp");
	}
}
