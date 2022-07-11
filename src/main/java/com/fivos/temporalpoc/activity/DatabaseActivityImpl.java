package com.fivos.temporalpoc.activity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import io.temporal.activity.Activity;
import io.temporal.api.enums.v1.WorkflowIdReusePolicy;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;

public class DatabaseActivityImpl implements DatabaseActivity {
	@Override
	public Boolean loadData() {
		try (Stream<Integer> stream = query()) {
			stream.forEach(this::runActivityWorkflow);
		}
		return true;
	}

	private Stream<Integer> query() {
		// TODO: Replace with DB query
		Random random = new Random();
		return random.ints(1000).boxed();
	}

	private void runActivityWorkflow(Integer value) {
		WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
		WorkflowClient client = WorkflowClient.newInstance(service);

			WorkflowOptions options = WorkflowOptions.newBuilder()
				.setTaskQueue(Shared.ACTIVITY_TASK_QUEUE)
				.setWorkflowIdReusePolicy(WorkflowIdReusePolicy.WORKFLOW_ID_REUSE_POLICY_ALLOW_DUPLICATE_FAILED_ONLY)
				.setWorkflowId(String.valueOf(value))
				.build();

			ActivityWorkflow activityWorkflow = client.newWorkflowStub(
				ActivityWorkflow.class,
				options);
		activityWorkflow.runWorkflow();
	}
}
