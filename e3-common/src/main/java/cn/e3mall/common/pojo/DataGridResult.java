package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class DataGridResult implements Serializable {

	private long total;
	private List<?> rows;
	
	public DataGridResult() {
		super();
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public DataGridResult(Integer total, List<?> rows) {
        this.total = total;
        this.rows = rows;
    }

    public DataGridResult(Long total, List<?> rows) {
        this.total = total.intValue();
        this.rows = rows;
    }

	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
}
