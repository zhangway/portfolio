package no.uit.zhangwei.runkeeper;

public interface Callback<T> {
	

    void success(T result, String json);


}