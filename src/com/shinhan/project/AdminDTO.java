package com.shinhan.project;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AdminDTO {
	private String admin_id;
	private String admin_pw;
}
