package com.shinhan.project;

import java.sql.Date;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ScheduleDTO {
	private Date tour_day;
	private Integer tour_id;
	private String activity;
	private String tour_time;
}
