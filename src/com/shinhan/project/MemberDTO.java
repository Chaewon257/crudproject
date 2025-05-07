package com.shinhan.project;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDTO {
	private String part_id;
	private String part_pw;
	private String part_name;
	private String email;
	private String phone;
}
