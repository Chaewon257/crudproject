package com.shinhan.project;

import java.sql.Date;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ToursDTO {
	private Integer tour_id;         
	private String tour_name;
	private  Date start_date;
	private Date end_date;
	private Integer tour_price;
	private Integer max_participants;
	private String tour_info;
}
