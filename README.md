### 2024-01-18

<details>
<summary> 7강 ~ 11강 </summary>
<div markdown="1">


- 7강 폰트작업
- 8강 프론트엔드 프레임워크 문법 비교
   - https://component-party.dev/ 
- 9강 스벨트5문법 
  - 스벨트 5 beta - 문서 - https://svelte-5-preview.vercel.app/docs ( 룬 문법 )
  - 스벨트 - 문서 - https://svelte.dev/docs/introduction
  - 스벨트킷 - 문서 - https://kit.svelte.dev/docs
  - 스벨트킷2 (스벨트5)기준으로 알려줘~ 이런 방식으로 gpt사용 권장
- 11강 

</div>
</details>

---
### 2024-01-19

<details>
<summary> 12강 ~ 14강 </summary>
<div markdown="2">

- 12강
  - 포메터 설정
- 13강 (https://www.youtube.com/watch?v=ns-24_2P2PM)
  - 소스코드는 routes에 작성
  - layout 사용 방법
- 14강 https://www.youtube.com/watch?v=0cBPMacdZR0
  - 소셜 로그인, 파일 업로드, 쿠키 관련 yml작업 

</div>
</details>

---
### 2024-01-21

<details>
<summary> 15강 ~ 25강 </summary>
<div markdown="3">

- 15강 https://www.youtube.com/watch?v=Cp2pM0UEqv4
- 16강 https://www.youtube.com/watch?v=iS2WYr0ohR4
  -  gitignore에 src/main/generated 추가 
- 17강 https://youtu.be/VaxUxLPMRLw
  -  initNotProd 샘플 생성 
- 18강 https://www.youtube.com/watch?v=VaxUxLPMRLw
  -  Get /api/v1/posts
- 19강 https://www.youtube.com/watch?v=-wwaeYTfpLA
  -  /p/list 작업
- 20강 https://www.youtube.com/watch?v=DwJDutYa52U
  -  openAPI 문서 정보를 토대로 통신 데이터와 관련된 타입스크립트를 생성 후 글 리스트에 적용
  - 자바랑 자바 스크립트랑 클래스의 정보 API관련된 정보는 swagger에 의해서 공유가 된다.
  - "npx openapi-typescript " + backUrl + "/v3/api-docs/apiV1 -o ./front/src/lib/types/api/v1/schema.d.ts"
  - 위 코드를 통하여 변환을 해준다
- 21강 https://www.youtube.com/watch?v=ttph4UKuhzs
  -  openapi-fetch 를 사용해서 fetch를 더 편하게
  -  라이브러리 - openapi-typescript - openapi-fetch - https://github.com/drwpow/openapi-typescript/tree/main/packages/openapi-fetch
- 22강 https://www.youtube.com/watch?v=Z7lXbq01snU
  -  백엔드 주소를 환경변수로 통합
- 23강 https://www.youtube.com/watch?v=cfa4S4gUqLI
  -  rq.svelte.ts 도입하여 자주 사용하는 로직 모아두기
- 24강 https://www.youtube.com/watch?v=0eTTkCl03hE
  -  글 상세보기 
- 25강
<details>
<summary>작업 1 : findById(id) 예외처리 코드를 단순화</summary>
<div markdown="1">

기존 코드(src/main/java/com/ll/rsv/domain/post/post/controller/ApiV1PostController.java)   
```java
Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-1", "존재하지 않는 글입니다."));
```   
새 코드(src/main/java/com/ll/rsv/domain/post/post/controller/ApiV1PostController.java)   
```java
Post post = postService.findById(id).orElseThrow(GlobalException.E404::new); // 기존 코드가 너무 길어서 이렇게 줄임
 ```
</div>
</details>

<details>
<summary>작업 2 : 예외발생시 출력되는 JSON의 내용을 커스터마이징</summary>
<div markdown="2">

새 코드(src/main/java/com/ll/rsv/global/exceptionHandlers/GlobalExceptionHandler.java)   
```java
package com.ll.rsv.global.exceptionHandlers;

import com.ll.rsv.global.exceptions.GlobalException;
import com.ll.rsv.global.rq.Rq;
import com.ll.rsv.global.rsData.RsData;
import com.ll.rsv.standard.base.Empty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
  private final Rq rq;

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleException(Exception ex) {
    // 아래 `throw ex;` 코드는 API 요청이 아닌 경우에만 실행된다.
    // if (rq.isApi()) throw ex; // 어짜피 이 서버(스프링부트)를 API서버로만 이용할 것이므로 이 코드는 필요 없다.

    return handleApiException(ex);
  }

  // 자연스럽게 발생시킨 예외처리
  private ResponseEntity<Object> handleApiException(Exception ex) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("resultCode", "500-1");
    body.put("statusCode", 500);
    body.put("msg", ex.getLocalizedMessage());

    LinkedHashMap<String, Object> data = new LinkedHashMap<>();
    body.put("data", data);

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    ex.printStackTrace(pw);
    data.put("trace", sw.toString().replace("\t", "    ").split("\\r\\n"));

    String path = rq.getCurrentUrlPath();
    data.put("path", path);

    body.put("success", false);
    body.put("fail", true);

    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // 개발자가 명시적으로 발생시킨 예외처리
  @ExceptionHandler(GlobalException.class)
  @ResponseStatus // 참고로 이 코드의 역할은 error 내용의 스키마를 타입스크립트화 하는데 있다.
  public ResponseEntity<RsData<Empty>> handle(GlobalException ex) {
    HttpStatus status = HttpStatus.valueOf(ex.getRsData().getStatusCode());
    rq.setStatusCode(ex.getRsData().getStatusCode());

    return new ResponseEntity<>(ex.getRsData(), status);
  }
}
```
</div>
</details>

<details>
<summary>작업 3 : 글 상세보기 페이지 구현</summary>
<div markdown="3">

새 코드(front/src/routes/p/[id]/+page.svelte)
```javascript
<script lang="ts">
  import { page } from '$app/stores';
  import rq from '$lib/rq/rq.svelte';

  async function load() {
    const { data, error } = await rq
      .apiEndPoints()
      .GET('/api/v1/posts/{id}', { params: { path: { id: parseInt($page.params.id) } } });

    // 이 코드가 실행되면 아래에 `{:catch error}` 부분으로 넘어감
    if (error) throw error;

    return data!;
  }
</script>

{#await load()}
  <div>loading...</div>
{:then { data: { item: post } }}
  <h1>{post.title}</h1>
  <div class="whitespace-pre-line">{post.body}</div>
{:catch error}
  <!-- .msg 로 접근할 수 있는 이유는 스프링부트의 에러관련 출력을 커스터마이징 했기 때문 -->
  {error.msg}
{/await}
```
</div>
</details>

</div>
</details>

----
### 2024-01-29

<details>
<summary> 26강 ~ </summary>
<div markdown="3">

- 26강 https://www.youtube.com/watch?v=cO0MH2iFzwU
  - 글 수정기능 구현
- 27강 https://www.youtube.com/watch?v=ejPoS8M8f4g
  - 글 수정 후 메세지와 redirect
- 28강 https://www.youtube.com/watch?v=PpwtevXexWA
  - 글 수정 시 공개여부도 같이 편집하도록 체크박스 도입
- 29강 https://www.youtube.com/watch?v=qBVXwbaGYsc
  - 로그인 폼 처리
- 30강 https://www.youtube.com/watch?v=HFRbVAKFmT8
  - 로그인 폼 구현, 성공 시 코드상에 보이지 않는 쿠기 2개 브라우저에 저장
  - 요청에 헤더와 바디 / 응답에 헤더와 바디가 존재
```javascript
const { data, error } = await rq.apiEndPoints().POST('/api/v1/members/login', {
      body: {
        username: form.username.value,
        password: form.password.value
      }
    });
// 응답의 바디는 정리되어 들어가지만 헤더는 보이지 않는다.
// 헤더 안에 쿠키가 들어가있다.
```

</div>
</details>