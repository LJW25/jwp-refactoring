# 키친포스

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

---

# 🎯 키친포스 요구사항

### 1. 주문 테이블

- [ ]  주문 테이블을 등록할 수 있다.
    - 새로 생성된 테이블은 그룹 정보를 가지지 않는다.
- [ ]  주문 테이블 목록를 조회할 수 있다.
- [ ]  주문 테이블의 상태를 변경할 수 있다.
    - 각 테이블은 비어있거나, 비어있지 않은 상태를 가진다.
    - empty=true이면 주문을 할 수 없다. empty=false이면 주문할 수 있다.
    - [ ]  `예외` 상태를 변경하려는 테이블이 존재하지 않으면, 예외가 발생한다.
    - [ ]  `예외` 상태를 변경하려는 테이블이 그룹에 포함되어 있을 경우 예외가 발생한다.
    - [ ]  `예외` 상태를 변경하려는 테이블의 주문 상태가 요리중이거나, 식사중일 경우 예외가 발생한다.
- [ ]  주문 테이블의 손님 수를 변경할 수 있다.
    - [ ]  `예외` 손님의 수는 음수일 수 없다.
    - [ ]  `예외` 존재하지 않는 테이블의 손님 수는 변경할 수 없다.
    - [ ]  `예외` 비어있는 테이블의 손님 수는 변경할 수 없다.

### 2. 테이블 그룹

- [ ]  테이블 그룹을 등록할 수 있다.
    - [ ]  `예외` 테이블 그룹은 최소 두개 이상의 테이블이 포함되어야 한다.
    - 테이블 그룹에 포함된 모든 테이블들을 주문 테이블로 등록한다.
        - [ ]  `예외` 테이블 그룹에 포함된 테이블 수와 주문 테이블로 등록된 테이블의 수는 같아야한다.
        - [ ]  `예외` 비어있는 테이블은 그룹에 포함될 수 없다.
        - [ ]  `예외` 이미 그룹에 포함된 테이블은 새로운 그룹에 포함될 수 없다.
    - 모든 테이블 그룹은 생성 날짜 정보를 가진다.
- [ ]  테이블 그룹을 해제할 수 있다.
    - [ ]  `예외` 해당 그룹 내에 요리중이거나, 식사중인 테이블이 있을 경우 그룹을 해제할 수 없다.
    - 테이블 그룹에 포함된 모든 테이블의 그룹 정보를 삭제한다.
    - 테이블 그룹에 포함된 모든 테이블을 사용 가능 상태로 변경한다.

### 3. 주문

- [ ]  주문을 접수할 수 있다.
    - 하나의 주문에는 한개 이상의 메뉴가 포함된다.
    - [ ]  `예외` 하나의 주문에 포함된 동일한 메뉴는 함께 계산한다.

즉, 동일한 메뉴를 두 개의 아이템으로 나누어 주문에 포함시킬 수없다.

    - [ ]  `예외` 존재하지 않는 테이블의 주문을 등록할 수 없다.
    - [ ]  `예외` 비어있는 테이블의 주문을 등록할 수 없다.
    - 정상적으로 주문이 접수되면 조리 상태가 된다.

- [ ]  주문 목록을 조회할 수 있다.
- [ ]  주문의 상태를 변경할 수 있다.
    - 주문의 상태는 `COOKING`(조리 중), `MEAL`(식사 중), `COMPLETION`(계산 완료) 3가지로 구분된다.
    - [ ]  `예외` 존재하지 않는 주문의 상태를 변경할 수 없다.
    - [ ]  `예외` 완료된 주문의 상태는 변경할 수 없다.

### 4. 상품

- [x]  상품을 등록할 수 있다.
    - [x]  `예외` 등록될 상품의 가격은 0원을 초과해야 한다.
- [x]  상품의 목록을 조회할 수 있다.

### 5. 메뉴

- [x]  메뉴 그룹을 등록할 수 있다.
- [x]  메뉴 그룹의 목록을 조회할 수 있다.
- [ ]  메뉴를 등록할 수 있다.
    - 하나의 메뉴에는 여러개의 상품이 포함될 수 있다.
    - [ ]  `예외` 등록될 메뉴의 가격은 0원을 초과해야 한다.
    - [ ]  `예외` 존재하지 않는 메뉴 그룹에 포함될 수 없다.
    - [ ]  `예외` 메뉴의 가격은 메뉴에 포함된 상품 가격의 총 합보다 작거나 같아야 한다.
- [ ]  메뉴의 목록을 조회할 수 있다.

---

## 🛠️ ERD

![ERD](https://github.com/woowacourse/jwp-refactoring/assets/49433615/56ee9408-f803-4966-9929-b325ab1f674a)

