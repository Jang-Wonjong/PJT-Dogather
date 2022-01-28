package com.dogather.pjtserver.controller;

import com.dogather.pjtserver.dto.UserDto;
import com.dogather.pjtserver.jwt.JwtProvider;
import com.dogather.pjtserver.jwt.JwtRet;
import com.dogather.pjtserver.service.UserService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	//회원가입
	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@RequestBody UserDto userDto){
		System.err.println("(Post)User Controller Register Method run!");

		int created = userService.userRegister(userDto);

		if (created == 1){
			userDto.setMsg("가입완료");
			return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}else{
			return new ResponseEntity<UserDto>(userDto, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//로그인
	@PostMapping("/login")
	public ResponseEntity<JwtRet> login(@RequestBody UserDto userDto){
		System.err.println("(Post)User Controller Login Method run!");
		JwtRet ret =  new JwtRet(); //return value for client by JSON

		// 로그인
		UserDto loginResult = userService.userLogin(userDto); // userService에 로그인 요청
		if (loginResult != null) {
			// 있는 아이디
			if (loginResult.getUserNo() == 0) {
				// 비밀번호 틀림
				System.err.println("로그인 요청 : 비밀번호 틀림!!!");
				ret.setMsg("wrongPw");
				return new ResponseEntity<JwtRet>(ret, HttpStatus.NOT_FOUND);
			}else {
				// 로그인 성공
				String jwt = JwtProvider.getToken(loginResult.getUserId());
				loginResult.setUserPw(null);
				ret.setJwt(jwt);
				ret.setMsg("success");
				ret.setUserInfo(loginResult);
				return new ResponseEntity<JwtRet>(ret, HttpStatus.OK);
			}
		}else {
			//없는 아이디
			System.err.println("로그인 요청 : 비밀번호 틀림!!!");
			ret.setMsg("wrongid");
			return new ResponseEntity<JwtRet>(ret, HttpStatus.NOT_FOUND);
		}

	}
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> update(@PathVariable String userId, @RequestHeader String jwt, @RequestBody UserDto userDto){
		System.err.println("(Put)User Controller Update Method run!");
		//JWT token check
		String validationResult = JwtProvider.validateToken(jwt, userId);
		if(userId.equals(validationResult)) {
			userDto.setUserId(userId);
			int created = userService.userUpdate(userDto);
			if (created == 1 ){
				// 수정완료
				return ResponseEntity.status(HttpStatus.OK).body(userDto);//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
			}
			//그외 오류?
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<String> delete(@PathVariable String userId, @RequestHeader String jwt){
		System.err.println("(Delete)User Controller delete Method run!");
		String validationResult = JwtProvider.validateToken(jwt, userId);
		if(userId.equals(validationResult)) {
			userService.userDelete(userId);
			return ResponseEntity.status(HttpStatus.OK).body(userId + " deleted completely!");
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> find(@PathVariable String userId, @RequestHeader String jwt){
		// 현재는 userId method jwt token 있어야 확인가능 => 본인것만 확인가능
		System.err.println("(Get)User Controller Find Method run!");
		//JWT token check
		String validationResult = JwtProvider.validateToken(jwt, userId);
		if(userId.equals(validationResult)) {
			UserDto userInfo = userService.userFind(userId);
			userInfo.setUserPw(null);
			return ResponseEntity.status(HttpStatus.OK).body(userInfo);//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
		}
	}

	@GetMapping("/idcheck")
	public ResponseEntity<String> idCheck(@RequestParam String id){
		System.err.println("(Get)User Controller idCheck Method run!");
		String result = userService.userIdCheck(id);
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("requested_id", id);
		return ResponseEntity.status(HttpStatus.OK).body(json.toString(4));//ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}

	@GetMapping("/nickcheck")
	public ResponseEntity<String> nickCheck(@RequestParam String nick){
		String result = userService.userNickCheck(nick);
		JSONObject json = new JSONObject();
		json.put("result", result);
		json.put("requested_id", nick);
		return new ResponseEntity<String>(json.toString(4), HttpStatus.OK);
	}

}
