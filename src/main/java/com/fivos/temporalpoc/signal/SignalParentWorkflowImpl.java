package com.fivos.temporalpoc.signal;

import java.time.Duration;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;

public class SignalParentWorkflowImpl implements SignalParentWorkflow {
	private boolean dataLoadingCompleted = false;
	private final ActivityOptions options = ActivityOptions.newBuilder()
		.setStartToCloseTimeout(Duration.ofMinutes(10))
		.build();

	private final SignalDatabaseActivity signalDatabaseActivity =
		Workflow.newActivityStub(SignalDatabaseActivity.class, options);

	@Override
	public void executeWorkflow() {
		signalDatabaseActivity.loadData();
		Workflow.await(() -> dataLoadingCompleted);
	}

	@Override
	public void sendSignal(Integer value) {
		System.out.println("Signal received with value:" + value);
		ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
			.setTaskQueue(SignalShared.SIGNAL_TASK_QUEUE)
			.setWorkflowId(String.valueOf(value))
			.setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
			.build();

		SignalChildWorkflow child = Workflow.newChildWorkflowStub(SignalChildWorkflow.class, options);
		child.executeWorkflow(value);
	}

	@Override
	public void dataLoadingCompleted() {
		System.out.println("Data loading completed signal");
		this.dataLoadingCompleted = true;
	}
}
