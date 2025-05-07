package com.shinhan.project;

import java.sql.Date;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingDTO {
	private Integer booking_no;
	private Integer tour_id;
	private String part_id;
	private Date booking_date;
	private String booking_status;
	private Integer paid_amount;
}
