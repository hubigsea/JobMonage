package com.ibigsea.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ibigsea.vo.PageTrigger;

@Repository("jobService")
public class JobService {

	@Autowired
	private Scheduler scheduler;
	
	/**
	 * 获取所有的job
	 * @return
	 */
	public List<JobDetail> getJobs(){
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
			
			List<JobDetail> jobDetails = new ArrayList<JobDetail>();
			
			for (JobKey key : jobKeys) {
				jobDetails.add(scheduler.getJobDetail(key));
			}
			return jobDetails;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		 
		 return null;
	}
	
	//获取所有的触发器
	public List<PageTrigger> getTriggersInfo(){
		try {
			GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
			Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);
			List<PageTrigger> triggers = new ArrayList<PageTrigger>();
			
			for (TriggerKey key : Keys) {
				Trigger trigger = scheduler.getTrigger(key);
				PageTrigger pageTrigger = new PageTrigger();
				pageTrigger.setName(trigger.getJobKey().getName());
				pageTrigger.setGroup(trigger.getJobKey().getGroup());
				pageTrigger.setStatus(scheduler.getTriggerState(key)+"");
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					pageTrigger.setExpression("重复次数:"+ (simple.getRepeatCount() == -1 ? 
							"无限" : simple.getRepeatCount()) +",重复间隔:"+(simple.getRepeatInterval()/1000L));
					pageTrigger.setDesc(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					pageTrigger.setExpression(cron.getCronExpression());
					pageTrigger.setDesc(cron.getDescription());
				}
				triggers.add(pageTrigger);
			}
			return triggers;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//暂停任务
	public void stopJob(String name, String group){
		JobKey key = new JobKey(name, group);
		try {
			scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	//恢复任务
	public void restartJob(String name, String group){
		JobKey key = new JobKey(name,group);
		try {
			scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	//立马执行一次任务
	public void startNowJob(String name, String group){
		try {
			JobKey key = new JobKey(name, group);
			JobDetail job = JobBuilder.newJob(
						scheduler.getJobDetail(key).getJobClass())
					.storeDurably()
					.build();
			scheduler.addJob(job, false);
			scheduler.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	//删除任务
	public void delJob(String name, String group){
		JobKey key = new JobKey(name,group);
		try {
			scheduler.deleteJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	//修改触发器时间
	public void modifyTrigger(String name,String group,String cron){
		try {
			TriggerKey key = TriggerKey.triggerKey(name, group);
			Trigger trigger = scheduler.getTrigger(key);
			
			CronTrigger newTrigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withIdentity(key)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron))
					.build();
			scheduler.rescheduleJob(key, newTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
	}
	
	//暂停调度器
	public void stopScheduler(){
		 try {
			if (!scheduler.isInStandbyMode()) {
				scheduler.standby();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	
}
