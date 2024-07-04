package com.example.demo.dto.order;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.List;

public class TotalOrderDto {
    private Long count;
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    private LocalDate time;
//    private LocalTime hour;

//    public LocalTime getHour() {
//        return hour;
//    }
//
//    public void setHour(LocalTime hour) {
//        this.hour = hour;
//    }

//    public TotalOrderDto(LocalDate hour, List<OrderResponse> orderResponses) {
//        this.hour = hour;
//        this.orderResponses = orderResponses;
//    }

    private List<OrderResponse> orderResponses;

//public TotalOrderDto(Long count, LocalDate time) {
//    super();
//    this.count = count;
//    this.time = time;
//}
public TotalOrderDto(LocalDate time, List<OrderResponse> orderResponses) {
    this.time = time;
    this.orderResponses = orderResponses;
}

    public List<OrderResponse> getOrderResponses() {
        return orderResponses;
    }

    public void setOrderResponses(List<OrderResponse> orderResponses) {
        this.orderResponses = orderResponses;
    }

    public Long getCount() {
        return count;
    }


    public void setCount(Long count) {
        this.count = count;
    }

	public LocalDate getTime() {
		return time;
	}

	public void setTime(LocalDate time) {
		this.time = time;
	}
//}
}


