package dao;

import java.util.List;

public interface Gestor<T> {
	
	public T getById(String id);
	
	public List<T> getAll();
	
	public List<T> getByNotDownloaded();
}
