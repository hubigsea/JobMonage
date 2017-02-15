package com.ibigsea.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 简单的job类
 * 实现job接口
 * @author sea
 *
 */
public class SimpleJob implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		System.out.println(dateFormat.format(new Date()) +" : " + context.getJobDetail().getKey());
	}

}
