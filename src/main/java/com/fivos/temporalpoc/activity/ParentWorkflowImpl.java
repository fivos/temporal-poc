package com.fivos.temporalpoc.activity;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

public class ParentWorkflowImpl implements ParentWorkflow {
	private final ActivityOptions options = ActivityOptions.newBuilder().validateAndBuildWithDefaults();
	private final DatabaseActivity databaseActivity =
		Workflow.newActivityStub(DatabaseActivity.class, options);

	@Override
	public void runWorkflow() {
		databaseActivity.loadData();
	}
}
