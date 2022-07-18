package com.fivos.temporalpoc.batch;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;

public class BatchStarter {
	private static final WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
	private static final WorkflowClient client = WorkflowClient.newInstance(service);
	private static final WorkerFactory factory = WorkerFactory.newInstance(client);

	public static void main(String[] args) {
		createWorker();

		WorkflowOptions parentWorkflowOptions =
			WorkflowOptions.newBuilder()
				.setWorkflowId("batchParentWorkflow")
				.setTaskQueue(BatchShared.BATCH_TASK_QUEUE)
				.build();
		BatchParentWorkflow parentWorkflowStub =
			client.newWorkflowStub(BatchParentWorkflow.class, parentWorkflowOptions);

		parentWorkflowStub.executeWorkflow();

		sleep(30);
	}


	private static void createWorker() {
		Worker worker = factory.newWorker(BatchShared.BATCH_TASK_QUEUE);
		worker.registerWorkflowImplementationTypes(BatchParentWorkflowImpl.class, BatchChildWorkflowImpl.class);
		worker.registerActivitiesImplementations(new BatchDatabaseActivityImpl());

		factory.start();
	}

	private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());
			System.exit(0);
		}
	}

}
