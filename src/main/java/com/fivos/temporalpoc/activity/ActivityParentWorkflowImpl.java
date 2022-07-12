package com.fivos.temporalpoc.activity;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class ActivityParentWorkflowImpl implements ActivityParentWorkflow {
	private final ActivityOptions options = ActivityOptions.newBuilder()
		.setStartToCloseTimeout(Duration.ofMinutes(10))
		.build();
	private final ActivityDatabaseActivity databaseActivity =
		Workflow.newActivityStub(ActivityDatabaseActivity.class, options);

	@Override
	public void executeWorkflow() {
		databaseActivity.loadData();
	}
}
