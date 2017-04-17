package com.creants.creants_2x.core.controllers;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

import org.slf4j.Logger;

import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.wood.RequestComparator;
import com.creants.creants_2x.socket.io.IRequest;

/**
 * @author LamHa
 *
 */
public abstract class AbstractController implements IController, Runnable {
	protected Object id;
	protected String name;
	protected BlockingQueue<IRequest> requestQueue;
	protected ExecutorService threadPool;
	protected int threadPoolSize;
	protected volatile int maxQueueSize;
	protected volatile boolean isActive;
	private volatile int threadId;

	public AbstractController() {
		threadPoolSize = -1;
		maxQueueSize = -1;
		isActive = false;
		threadId = 1;
	}

	@Override
	public void enqueueRequest(final IRequest request) throws Exception {
		if (this.requestQueue.size() >= this.maxQueueSize) {
			throw new Exception("Full queue");
		}
		this.requestQueue.add(request);
	}

	@Override
	public void init(final Object o) {
		if (isActive) {
			throw new IllegalArgumentException("Object is already initialized. Destroy it first!");
		}
		if (threadPoolSize < 1) {
			throw new IllegalArgumentException("Illegal value for a thread pool size: " + this.threadPoolSize);
		}
		if (maxQueueSize < 1) {
			throw new IllegalArgumentException("Illegal value for max queue size: " + this.maxQueueSize);
		}

		final Comparator<IRequest> requestComparator = new RequestComparator();
		requestQueue = new PriorityBlockingQueue<IRequest>(50, requestComparator);
		threadPool = Executors.newFixedThreadPool(threadPoolSize);
		isActive = true;
		initThreadPool();
		QAntTracer.info(this.getClass(), String.format("Controller started: %s -- Queue: %s/%s",
				this.getClass().getName(), getQueueSize(), this.getMaxQueueSize()));
	}

	@Override
	public void destroy(final Object o) {
		this.isActive = false;
		final List<Runnable> leftOvers = this.threadPool.shutdownNow();
		this.logger
				.info("Controller stopping: " + this.getClass().getName() + ", Unprocessed tasks: " + leftOvers.size());
	}

	@Override
	public void handleMessage(final Object message) {
	}

	@Override
	public void run() {
		Thread.currentThread().setName(String.valueOf(this.getClass().getName()) + "-" + this.threadId++);
		while (this.isActive) {
			try {
				final IRequest request = this.requestQueue.take();
				this.processRequest(request);
			} catch (InterruptedException e) {
				this.isActive = false;
				this.logger.warn("Controller main loop was interrupted");
			} catch (Throwable t) {
				QAntTracer.error(AbstractController.class, "Runable fail!");
			}
		}
		this.bootLogger.info("Controller worker threads stopped: " + this.getClass().getName());
	}

	public abstract void processRequest(final IRequest p0) throws Exception;

	@Override
	public Object getId() {
		return this.id;
	}

	@Override
	public void setId(final Object id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int getThreadPoolSize() {
		return this.threadPoolSize;
	}

	@Override
	public void setThreadPoolSize(final int threadPoolSize) {
		if (this.threadPoolSize < 1) {
			this.threadPoolSize = threadPoolSize;
		}
	}

	@Override
	public int getQueueSize() {
		return this.requestQueue.size();
	}

	@Override
	public int getMaxQueueSize() {
		return this.maxQueueSize;
	}

	@Override
	public void setMaxQueueSize(final int maxQueueSize) {
		this.maxQueueSize = maxQueueSize;
	}

	protected void initThreadPool() {
		for (int j = 0; j < this.threadPoolSize; ++j) {
			this.threadPool.execute(this);
		}
	}
}
