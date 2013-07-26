package de.gdxgame;

import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A thread-safe {@link T} list which does not allow {@code null} elements.
 */
public class ThreadSafeList<T> implements Iterable<T>, RandomAccess
{
	private void checkIsNull(T t)
	{
		if (t == null)
		{
			throw new IllegalArgumentException("item must not be null");
		}
	}

	private final CopyOnWriteArrayList<T> TsList;

	public ThreadSafeList()
	{
		this.TsList = new CopyOnWriteArrayList<T>();
	}

	/**
	 * @see List#add(int, Object)
	 */
	public synchronized void add(int index, T t)
	{
		checkIsNull(t);
		this.TsList.add(index, t);
	}

	/**
	 * @see List#add(Object)
	 */
	public synchronized void add(T t)
	{
		checkIsNull(t);
		this.TsList.add(t);
	}

	/**
	 * @see List#clear()
	 */
	public synchronized void clear()
	{
		this.TsList.clear();
	}

	/**
	 * @see List#contains(Object)
	 */
	public synchronized boolean contains(T t)
	{
		checkIsNull(t);
		return this.TsList.contains(t);
	}

	/**
	 * @see List#get(int)
	 */
	public synchronized T get(int index)
	{
		return this.TsList.get(index);
	}

	/**
	 * @see List#indexOf(Object)
	 */
	public synchronized int indexOf(T T)
	{
		checkIsNull(T);
		return this.TsList.indexOf(T);
	}

	/**
	 * @see List#isEmpty()
	 */
	public synchronized boolean isEmpty()
	{
		return this.TsList.isEmpty();
	}

	/**
	 * @see List#iterator()
	 */
	@Override
	public synchronized Iterator<T> iterator()
	{
		return this.TsList.iterator();
	}

	/**
	 * @see List#remove(int)
	 */
	public synchronized T remove(int index)
	{
		T t = this.TsList.remove(index);
		return t;
	}

	/**
	 * @see List#remove(Object)
	 */
	public synchronized boolean remove(T t)
	{
		checkIsNull(t);
		if (this.TsList.remove(t))
		{
			return true;
		}
		return false;
	}

	/**
	 * @see List#size()
	 */
	public synchronized int size()
	{
		return this.TsList.size();
	}

	public synchronized void trimToSize()
	{
		// TODO this.TsList.trimToSize();
	}

	public synchronized void addAll(ThreadSafeList<T> list)
	{
		for (T t : list)
		{
			checkIsNull(t);
			add(t);
		}
	}
}
