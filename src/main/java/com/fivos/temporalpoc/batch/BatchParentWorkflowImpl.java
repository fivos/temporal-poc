package com.fivos.temporalpoc.batch;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.temporal.activity.ActivityOptions;
import io.temporal.api.enums.v1.ParentClosePolicy;
import io.temporal.workflow.Async;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;

public class BatchParentWorkflowImpl implements BatchParentWorkflow {
	private static final int BATCH_SIZE = 10;
	private WorkflowQueue<Integer> workflowQueue = Workflow.newWorkflowQueue(Integer.MAX_VALUE);
	private Set<Integer> batch = new HashSet<>();
	private int batchCounter = 0;
	private boolean dataLoadingCompleted = false;
	private List<Promise<Boolean>> childWorkflowPromises = new ArrayList<>();
	private final ActivityOptions options = ActivityOptions.newBuilder()
		.setStartToCloseTimeout(Duration.ofMinutes(10))
		.build();

	private final BatchDatabaseActivity databaseActivity =
		Workflow.newActivityStub(BatchDatabaseActivity.class, options);

	@Override
	public void executeWorkflow() {
		Async.function(databaseActivity::loadData);
		Workflow.await(() -> dataLoadingCompleted);

		while (true) {
			// Wait until there is an item on the queue or account loading has completed
			Workflow.await(() -> workflowQueue.peek() != null || dataLoadingCompleted);

			Integer value = workflowQueue.poll();
			if (value != null) {
				batch.add(value);

				if (batch.size() >=  BATCH_SIZE) {
					processAccountIdBatch();
				}
				continue;
			}

			if (dataLoadingCompleted) {
				if (batch.size() > 0) {
					processAccountIdBatch();
				}
				break;
			}
		}

		for (Promise<Boolean> promise : childWorkflowPromises) {
			promise.get();
		}

	}

	@Override
	public void receiveValue(Integer value) {
		workflowQueue.put(value);
	}

	@Override
	public void dataLoadingCompleted() {
		this.dataLoadingCompleted = true;
	}

	private void processAccountIdBatch() {
		ChildWorkflowOptions options = ChildWorkflowOptions.newBuilder()
			.setTaskQueue(BatchShared.BATCH_TASK_QUEUE)
			.setWorkflowId("BatchChildWorkflow-" + batchCounter)
			.setParentClosePolicy(ParentClosePolicy.PARENT_CLOSE_POLICY_ABANDON)
			.build();

		BatchChildWorkflow child = Workflow.newChildWorkflowStub(BatchChildWorkflow.class, options);
		childWorkflowPromises.add(Async.function(child::executeWorkflow, batch));
		batchCounter++;
		batch.clear();
	}
}
