package com.abc.test.cabapp.cache;

import java.lang.reflect.Method;
import java.time.LocalDate;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import com.abc.test.cabapp.common.util.Utility;

@Component
public class CustomTriReportKeyGenerator implements KeyGenerator{

	@Override
	public Object generate(Object target, Method method, Object... params) {
		LocalDate date = (LocalDate)params[1];
		return new StringBuilder().append(params[0].toString()).append("-").append(Utility.formatter.format(date)).toString();
	}

}
