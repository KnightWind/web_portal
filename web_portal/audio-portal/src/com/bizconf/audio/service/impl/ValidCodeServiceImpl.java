package com.bizconf.audio.service.impl;

import java.sql.SQLException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.bizconf.audio.component.language.LanguageHolder;
import com.bizconf.audio.entity.ValidCode;
import com.bizconf.audio.service.ValidCodeService;

@Service
public class ValidCodeServiceImpl extends BaseService implements ValidCodeService {

	@Override
	public boolean checkValidCode(String random, String type, String input) {
		String sessionId = LanguageHolder.getTSessionID();
		if (sessionId == null || sessionId.length() == 0) {
			return false;
		}
//		e42153e5-9e64-4fe9-a4ec-27da64e8b15c.syslogin.1362548177503
		String codeKey = genCodeKey(sessionId, type, random);
		try {
			ValidCode code = libernate.getEntity(ValidCode.class, codeKey);
			if (code == null) {
				return false;
			}
			
			if (!code.getCode().equalsIgnoreCase(input)) {
				return false;
			}
			
			this.remove(code);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public void recordValidCode(String random, String type, String code) {
		String codeKey = this.genCodeKey(LanguageHolder.getTSessionID(), type, random);
		try {
			ValidCode checkCode = libernate.getEntity(ValidCode.class, codeKey);
			if (checkCode != null) {
				checkCode.setCode(code);
				checkCode.setCreateTime(new Date());
				libernate.updateEntity(checkCode);
				return;
			}
			
			//create new code
			ValidCode validCode = new ValidCode();
			validCode.setCreateTime(new Date());
			validCode.setCode(code);
			validCode.setCodeKey(codeKey);
			libernate.saveEntity(validCode);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void remove(ValidCode code) {
		try {
			libernate.deleteEntity(code);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String genCodeKey(String tsessionId, String type, String random) {
		return tsessionId + "." + type + "." + random;
	}
}
