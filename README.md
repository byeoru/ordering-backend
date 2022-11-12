Ordering Server

DB
![erd](https://user-images.githubusercontent.com/60863965/201467410-bbcec67c-96f8-4aaa-855d-eb70f93a5157.png)

Request API
<aside>
📌 **계정 관련 API**

</aside>

## 고객 인증번호를 요청하는 API

URL: http://www.ordering.ml/api/customer/verification/get

Http method: POST

Request Object: PhoneNumberDto

Response Object: ResultDto<Boolean>

사용가능한 번호이면 TRUE, 이미 가입된 번호이면 FALSE를 반환, 오류 시 예외 발생

## 점주 인증번호를 요청하는 API

URL: http://www.ordering.ml/api/owner/verification/get

Http method: POST

Request Object: PhoneNumberDto

Response Object: ResultDto<Boolean>

사용가능한 번호이면 TRUE, 이미 가입된 번호이면 FALSE를 반환, 오류 시 예외 발생

## 고객 인증번호를 체크하는 API

URL: http://www.ordering.ml/api/customer/verification/check

Http method: POST

Request Object: VerificationDto

Response Object: ResultDto<Boolean>

인증번호가 올바르면 TRUE, 틀렸으면 FALSE를 반환, 오류 시 예외 발생

## 점주 인증번호를 체크하는 API

URL: http://www.ordering.ml/api/owner/verification/check

Http method: POST

Request Object: VerificationDto

Response Object: ResultDto<Boolean>

인증번호가 올바르면 TRUE, 틀렸으면 FALSE를 반환, 오류 시 예외 발생

## 고객 회원가입 API

URL: http://www.ordering.ml/api/customer/signup

Http method: POST

Request Object: CustomerSignUpDto

Response Object: ResultDto<Long>

회원가입에 성공했으면 회원 primary key, 이미 가입된 아이디이면 NULL 반환, 오류 시 예외 발생

## 점주 회원가입 API

URL: http://www.ordering.ml/api/owner/signup

Http method: POST

Request Object: OwnerSignUpDto

Response Object: ResultDto<Long>

회원가입에 성공했으면 회원 primary key, 이미 가입된 아이디이면 NULL 반환, 오류 시 예외 발생

## 고객 로그인 API

URL: http://www.ordering.ml/api/customer/signin

Http method: POST

Request Object: SignInDto

Response Object: ResultDto<Long>

로그인 성공 시 회원 primary key, id or password가 틀렸으면 NULL 반환, 오류 시 예외 발생

## 점주 로그인 API

URL: http://www.ordering.ml/api/owner/signin

Http method: POST

Request Object: SignInDto

Response Object: ResultDto<`OwnerSignInResultDto`>

로그인 성공 시 `OwnerSignInResultDto` 반환, id or password 가 틀렸을 시 NULL 반환, 오류 시 예외 발생

## 고객 로그아웃 API

URL: http://www.ordering.ml/api/customer/{customerId}/sign_out

Http method: POST

Request Object: NONE

Response Object: `ResultDto<Boolean>`

로그아웃 성공 시 TRUE 반환, 오류 시 예외 발생

## 점주 로그아웃 API

URL: http://www.ordering.ml/api/owner/{ownerId}/sign_out

Http method: POST

Request Object: NONE

Response Object: `ResultDto<Boolean>`

로그아웃 성공 시 TRUE 반환, 오류 시 예외 발생

## 고객 비밀번호 변경 API

URL: http://www.ordering.ml/api/customer/{customer_id}/password

Http method: PUT

Request Object: `PasswordChangeDto`

Response Object: ResultDto<Boolean>

비밀번호 변경 성공 시 TRUE, 현재비밀번호 불일치 시 FALSE를 반환하고 오류 시 예외를 발생

## 점주 비밀번호 변경 API

URL: http://www.ordering.ml/api/owner/{owner_id}/password

Http method: PUT

Request Object: `PasswordChangeDto`

Response Object: ResultDto<Boolean>

비밀번호 변경 성공 시 TRUE, 현재비밀번호 불일치 시 FALSE를 반환하고 오류 시 예외를 발생

## 고객 휴대폰번호 변경 API

URL: http://www.ordering.ml/api/customer/{customer_id}/phone_number

Http method: PUT

Request Object: PhoneNumberDto

Response Object: ResultDto<Boolean>

휴대폰번호 변경 성공 시 TRUE를 반환, 오류 시 예외를 발생 (예외 처리만 하면 됨)

<aside>
🚧 현재 쿼리 4방 나감. 최적화 고민 해볼 것, 최종: 이제 2방 나감!

</aside>

## 점주 휴대폰번호 변경 API

URL: http://www.ordering.ml/api/owner/{owner_id}/phone_number

Http method: PUT

Request Object: PhoneNumberDto

Response Object: ResultDto<Boolean>

휴대폰번호 변경 성공 시 TRUE를 반환, 오류 시 예외를 발생 (예외 처리만 하면 됨)

<aside>
🚧 현재 쿼리 4방 나감. 최적화 고민 해볼 것, 최종: 이제 2방 나감!

</aside>

## 고객 계정삭제 API

URL: http://www.ordering.ml/api/customer/{customer_id}

Http method: DELETE

Request Object: None

Response Object: ResultDto<Boolean>

계정삭제 성공 시 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

## 점주 계정삭제 API

URL: http://www.ordering.ml/api/owner/{owner_id}

Http method: DELETE

Request Object: None

Response Object: ResultDto<Boolean>

계정삭제 성공 시 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

<aside>
📌 **매장 관련 API**

</aside>

## 점주 매장 등록  API

URL: http://www.ordering.ml/api/owner/{owner_id}/restaurant

Http method: POST

Request Object: `RestaurantDataWithLocationDto`

Response Object: ResultDto<Long>

매장정보 등록에 성공했으면 매장 primary key를 반환하고 오류 시 예외를 발생

- [x]  매장정보 변경 API 따로 구현할 것

## 점주 매장정보 변경  API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}

Http method: PUT

Request Object: `RestaurantDataWithLocationDto`

Response Object: ResultDto<Boolean>

매장정보 변경에 성공했으면 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

## 매장 음식 추가 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/food

Http method: POST

Request Object: `FoodDto(required = true)`, `MultipartFile(required = false)`

Response Object: ResultDto<Long>

음식 등록에 성공했으면 음식 primary key를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

<aside>
🚧 현재 쿼리 3방 나감. 최적화 고민 해볼 것 → 3방에서 2방으로 줄였음ㅎㅎ

</aside>

## 매장 음식 정보 변경 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/food/{food_id}

Http method: PUT

Request Object: `FoodDto(required = true)`, `MultipartFile(required = false)`

Response Object: ResultDto<Boolean>

매장 음식 정보 변경 성공 시 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

## 매장 음식 품절 정보 변경 API

URL: http://www.ordering.ml/api/restaurant/food/{food_id}/status

Http method: PUT

Request Object: `FoodStatusDto`

Response Object: ResultDto<Boolean>

매장 음식 품절 정보 변경 성공 시 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

## 매장 음식 삭제 API

URL: http://www.ordering.ml/api/restaurant/food/{food_id}

Http method: DELETE

Request Object: None

Response Object: ResultDto<Boolean>

매장 음식 삭제 성공 시 TRUE를 반환하고 오류 시 예외를 발생 (예외 처리만 하면 됨)

## 매장 모든 음식 불러오기 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/foods

Http method: POST

Request Object: None

Response Object: ResultDto<List<FoodDto>>

음식 목록을 List에 담아서 반환, 오류 시 예외를 발생

## 매장 특정 달의 일별 매출 불러오기 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/daily_sales

Http method: POST

Request Object: `SalesRequestDto`

Response Object: `ResultDto<List<SalesResponseDto>>`

한달 매출을 일별로 List에 담아서 반환, 오류 시 예외를 발생

## 매장 특정 년도의 월별 매출 불러오기 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/monthly_sales

Http method: POST

Request Object: `SalesRequestDto`

Response Object: `ResultDto<List<SalesResponseDto>>`

년 매출을 월별로 List에 담아서 반환, 오류 시 예외를 발생

## 매장 프로필 이미지 변경 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/profile_image

Http method: PUT

Request Object: `MultipartFile`

Response Object: `ResultDto<Boolean>`

변경 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 배경 이미지 변경 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/background_image

Http method: PUT

Request Object: `MultipartFile`

Response Object: `ResultDto<Boolean>`

변경 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 대표 음식 추가 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/representative?food_id={food_id}

Http method: POST

Request Object: NONE

Response Object: `ResultDto<Boolean>`

추가 성공 시 TRUE를 똑같은 음식을 추가하려고 시도 시 FALSE를 반환, 오류 시 예외를 발생

## 매장 대표 음식 삭제 API

URL: http://www.ordering.ml/api/restaurant/representative/{representative_id}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

삭제 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 대표 음식 리스트 조회 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/representatives

Http method: GET

Request Object: NONE

Response Object: `ResultDto<List<RepresentativeMenuDto>>`

대표 음식리스트를 반환, 오류 시 예외 발생

## 위치기반 매장 목록 반환 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurants

Http method: POST

Request Object: `RestaurantPreviewListReqDto`

Response Object: `ResultDto<List<RestaurantPreviewWithDistanceDto>>`

매장리스트를 반환, 오류 시 예외 발생

## 매장 주문 예상 대기시간 설정/변경 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/order_waiting_time

Http method: PUT

Request Object: `WaitingTimeDto`

Response Object: `ResultDto<Boolean>`

설정/변경 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 웨이팅 입장 예상 대기시간 설정/변경 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/admission_waiting_time

Http method: PUT

Request Object: `WaitingTimeDto`

Response Object: `ResultDto<Boolean>`

설정/변경 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 preview data 반환 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/preview

Http method: POST

Request Object: NONE

Response Object: `ResultDto<RestaurantPreviewDto>`

restaurant_id에 해당하는 매장 preview 정보를 반환, 오류 시 예외 발생

## 매장 공지 작성/수정 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/notice

Http method: PUT

Request Object: `MessageDto`

Response Object: `ResultDto<Boolean>`

공지 작성 성공 시 TRUE를 반환, 오류 시 예외 발생

## 매장 소개 정보(좌표 포함) 반환 API

URL: http://www.ordering.ml/api/restaurant/{restaurant_id}/info

Http method: GET

Request Object: NONE

Response Object: `ResultDto<RestaurantInfoDto>`

매장 소개(좌표 포함) 정보를 반환, 오류 시 예외 발생

<aside>
📌 **찜(북마크) 관련 API**

</aside>

## 매장 찜(북마크) API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/bookmark?restaurant_id={restaurant_id}

Http method: POST

Request Object: NONE

Response Object: `ResultDto<Long>`

북마크 성공 시 북마크 primary key, 이미 북마크 된 매장을 중복으로 북마크하려고 할 시 NULL반환, 오류 시 예외 발생

## 매장 찜(북마크) 삭제 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/bookmark/{bookmarkId}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

북마크 삭제에 성공 시 TRUE를 반환, 오류 시 예외 발생

## 내 찜(북마크) 리스트 조회 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/bookmarks

Http method: GET

Request Object: NONE

Response Object: `ResultDto<List<BookmarkPreviewDto>>`

내 북마크 리스트를 반환, 오류 시 예외 발생

<aside>
📌 **장바구니**, **주문 관련 API**

</aside>

## 장바구니에 음식 추가 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order/basket?customer_id={customer_id}&restaurant_id={restaurant_id}

Http method: POST

Request Object: `BasketRequestDto`

Response Object: `ResultDto<Boolean>`

장바구니 추가에 성공하거나 이미 장바구니에 담긴 음식을 다시 담아서 수량을 늘리는데 성공했을 경우 TRUE, 서로 다른 음식점의 음식을 장바구니에 추가하려고 하는 경우 FALSE를 반환, 오류 시 예외 발생

## 장바구니 음식 수량 변경 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order/baskets?customer_id={customer_id}

Http method: PUT

Request Object: `List<BasketPutDto>`

Response Object: `ResultDto<Boolean>`

장바구니에 담긴 음식들의 수량 변경에 성공했을 경우 TRUE, 오류 시 예외 발생

## 장바구니에서 음식 삭제 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order/basket/{basket_id}?customer_id={customer_id}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

삭제 성공 시 TRUE, 오류 시 예외 발생 (예외 처리만 하면 됨)

## 장바구니 비우기 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order/baskets?customer_id={customer_id}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

삭제 성공 시 TRUE, 오류 시 예외 발생 (예외 처리만 하면 됨)

## 장바구니 리스트 조회 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/baskets

Http method: POST

Request Object: NONE

Response Object: `ResultDto<BasketListResultDto>`

장바구니 목록을 List에 담아서 반환, 오류 시 예외를 발생

## 장바구니에 담긴 음식 주문 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order?customer_id={customer_id}

Http method: POST

Request Object: `OrderDto`

Response Object: `ResultDto<Long>`

주문에 성공하면 주문 primary key, FCM이 실패하면 NULL을 반환하고 오류 시 예외 발생

<aside>
🚧 나중에 반드시 최적화

</aside>

## 고객 음식 주문 취소 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/order/{order_id}/cancel

Http method: POST

Request Object: None

Response Object: `ResultDto<OrderPreviewDto>`

주문 취소에 성공하면 **상태가 변경된 후의 데이터**를 DTO에 담아서 반환(주문 상태가 ORDERED일 경우), 취소 불가능할 경우에는 NULL을 반환, 그리고 오류 시에는 예외를 발생

## 점주 음식 주문 취소 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/order/{order_id}/owner_cancel

Http method: POST

Request Object: `MessageDto`

Response Object: `ResultDto<OrderPreviewDto>`

주문 취소에 성공하면 **상태가 변경된 후의 데이터**를 DTO에 담아서 반환(주문 상태가 ORDERED, CHECKED일 경우), 취소 불가능할 경우에는 NULL을 반환, 그리고 오류 시에는 예외를 발생

## 고객 음식 주문 체크 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/order/{order_id}/check

Http method: POST

Request Object: None

Response Object: `ResultDto<OrderPreviewDto>`

주문 체크에 성공하면 해당 주문에 주문 시간이 적용되며 **상태가 변경된 후의 데이터**를 DTO에 담아서 반환(주문 상태가 ORDERED일 경우), 체크 불가능할 경우에는 NULL을 반환, 그리고 오류 시에는 예외를 발생

## 고객 음식 주문 완료 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/order/{order_id}/complete

Http method: POST

Request Object: None

Response Object: `ResultDto<OrderPreviewDto>`

주문 완료에 성공하면 **상태가 변경된 후의 데이터**를 DTO에 담아서 반환(주문 상태가 CHECKED일 경우), 완료 불가능할 경우에는 NULL을 반환, 그리고 오류 시에는 예외를 발생

## 진행중인 주문(ORDERED, CHECKED) 리스트 반환 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurantId}/orders/ongoing

Http method: GET

Request Object: None

Response Object: `ResultDto<List<OrderPreviewDto>>`

진행 중인 주문 리스트를 반환, 오류 시 예외 발생

## 완료된 주문(CANCELED, COMPLETED) 리스트 반환 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurantId}/orders/finished

Http method: GET

Request Object: None

Response Object: `ResultDto<List<OrderPreviewDto>>`

완료된 주문 리스트를 반환, 오류 시 예외 발생

## 내 주문 내역(진행 중) 리스트 반환 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/orders/ongoing

Http method: GET

Request Object: None

Response Object: `ResultDto<List<OrderPreviewWithRestSimpleDto>>`

진행 중인 내 주문 리스트를 반환, 오류 시 예외 발생

## 내 주문 내역(완료) 리스트 반환 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/orders/finished?offset={offset}&limit={limit}

Http method: GET

Request Object: None

Response Object: `ResultDto<List<OrderPreviewWithRestSimpleDto>>`

완료된 내 주문 리스트를 반환, 오류 시 예외 발생

## 내 주문 상세정보 반환 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/order/{orderId}/detail

Http method: GET

Request Object: None

Response Object: `ResultDto<OrderDetailDto>` 

내 주문 상세정보를 반환, 오류 시 예외 발생

## 내 최근 주문 매장 리스트 반환(10개) API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customerId}/orders/recent

Http method: GET

Request Object: None

Response Object: `ResultDto<List<RecentOrderRestaurantDto>>`

내 최근 주문 매장 리스트를 반환, 오류 시 예외 발생

<aside>
📌 **웨이팅 관련 API**

</aside>

## 웨이팅 접수 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/waiting?restaurant_id={restaurant_id}&customer_id={customer_id}

Http method: POST

Request Object: `WaitingRegisterDto`

Response Object: `ResultDto<Boolean>`

웨이팅 접수에 성공하면 TRUE, 이미 다른 매장 또는 현재 접수하려는 매장에 웨이팅 접수를 한 상태라면 FALSE를 반환, 그리고 오류 시에는 예외를 발생

## 웨이팅 취소 API (고객용 앱, 점주용 앱에서 사용)

URL: http://www.ordering.ml/api/waiting/{waiting_id}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

웨이팅 취소에 성공하면 TRUE를 반환, 오류 시 예외 발생

## 내 웨이팅 정보 조회 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/{customer_id}/waiting

Http method: POST

Request Object: NONE

Response Object: `ResultDto<MyWaitingInfoDto>`

내 웨이팅 정보 반환, 웨이팅 정보가 없을 경우 null 반환, 오류 시 예외 발생

## 매장 웨이팅 호출 API (점주용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/waiting/{waitingId}/call

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

웨이팅 호출 성공 시 TRUE 반환, 오류 시 예외 발생

<aside>
📌 **리뷰 관련 API**

</aside>

## 주문한 음식 리뷰 작성하기 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/review?restaurant_id={restaurant_id}&order_id={order_id}

Http method: POST

Request Object: `ReviewDto(required = true)`, `MultipartFile(required = false)`

Response Object: `ResultDto<Boolean>`

리뷰 작성 성공 시 TRUE, 이미 해당 주문에 리뷰를 작성했으면 FALSE를 반환, 그리고 오류 시 예외를 발생

## 리뷰 삭제하기 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/customer/review/{review_id}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

리뷰 삭제 성공 시 TRUE를 반환, 오류 시 예외를 발생

## 매장 리뷰 리스트 조회 API (고객용 앱에서 사용)

URL: http://www.ordering.ml/api/restaurant/{restaurantId}/reviews

Http method: GET

Request Object: NONE

Response Object: `ResultDto<List<ReviewPreviewDto>>`

매장 리뷰 리스트 반환, 오류 시 예외를 발생

<aside>
📌 **쿠폰 관련 API**

</aside>

## 쿠폰 등록 API

URL: http://www.ordering.ml/api/coupon

Http method: POST

Request Object: `CouponDto`

Response Object: `ResultDto<Boolean>`

쿠폰 등록 성공 시 TRUE를 반환, 오류 또는 똑같은 serial number를 등록하려고 시도할 경우 예외를 발생 

## 고객 쿠폰 등록 API

URL: http://www.ordering.ml/api/customer/{customer_Id}/coupon

Http method: POST

Request Object: `CouponSerialNumberDto`

Response Object: `ResultDto<Boolean>`

쿠폰 등록 성공 시 TRUE를 반환, 이미 해당 쿠폰을 가지고 있을 시 FALSE를 반환, 존재하지 않는 쿠폰(serial number)이면 예외를 발생

## 고객 쿠폰 사용(삭제) API

URL: http://www.ordering.ml/api/customer/coupon/{couponId}

Http method: DELETE

Request Object: NONE

Response Object: `ResultDto<Boolean>`

사용(삭제) 성공 시 TRUE를 반환, 오류 시 예외를 발생

## 고객 쿠폰 리스트 조회 API

URL: http://www.ordering.ml/api/customer/{customer_Id}/my_coupons

Http method: POST

Request Object: NONE

Response Object: `ResultDto<List<CouponDto>>`

내 쿠폰 리스트를 반환, 오류 시 예외를 발생
