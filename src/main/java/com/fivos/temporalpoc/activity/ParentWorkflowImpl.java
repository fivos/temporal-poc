package com.fivos.temporalpoc.activity;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

public class ParentWorkflowImpl implements ParentWorkflow {
	private final ActivityOptions options = ActivityOptions.newBuilder()
		.setStartToCloseTimeout(Duration.ofMinutes(10))
		.build();
	private final DatabaseActivity databaseActivity =
		Workflow.newActivityStub(DatabaseActivity.class, options);

	@Override
	public void executeWorkflow() {
		databaseActivity.loadData();
	}
}
