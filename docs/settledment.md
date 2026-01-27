# 정산(Settlement) 도메인 사양

## 1. 목적

Settlement는 결제 완료된 Payment를 기준으로 정산 금액을 계산하고 기록하는 도메인이다.

Settlement는 회계 감사 및 추적성을 보장하기 위해 불변성을 가지며, 생성 이후 금액이나 대상은 변경되지 않는다.

---

## 2. 정산(Settlement) 타입

Settlement는 생성 목적에 따라 두 가지 타입을 가진다.

### 2.1 NORMAL

- Payment가 SUCCESS(결제 완료) 상태가 될 때 자동 생성된다.
- 일반적인 구독 결제에 대한 정산을 담당한다.
- Payment 1건당 1개의 NORMAL Settlement가 생성된다.

### 2.2 ADJUSTMENT

- 고객 컴플레인, 운영 정책, 경영진 승인 등에 의해 수동 생성된다.
- 기존 Settlement를 수정하거나 취소하지 않고, 보정(정정)을 위해 추가로 생성되는 정산이다.
- 반드시 원본 Settlement를 참조한다.

---

## 3. 정산(Settlement) 상태

Settlement는 다음과 같은 상태를 가진다.

### 3.1 READY

- Settlement가 생성된 상태
- 아직 실제 지급(정산)이 이루어지지 않은 상태

### 3.2 COMPLETED

- 정산이 완료되어 지급이 확정된 상태
- 이후 상태 변경 및 수정은 허용되지 않는다.

### 3.3 CANCELED

- 지급 이전에 오류 또는 결제 취소로 인해 무효 처리된 상태
- COMPLETED 상태에서는 CANCELED로 변경할 수 없다.

---

## 4. 상태 전환 규칙

Settlement의 상태 전이는 다음 규칙을 따른다.

- READY -> COMPLETED
  - 정산 지급 처리 완료

- READY -> CANCELED
  - 결제 취소, 오류 등으로 정산 무효 처리

- COMPLETED -> (변경 불가)
  - 완료된 정산은 불변이다.

---

## 5. 불변성 규칙

Settlement는 생성 이후 다음 항목을 변경 할 수 없다.

- 정산 금액
- 정산 대상(수령자)
- 정산 비율
- Settlement Type

정산 완료(COMPLETED) 이후의 변경 요청은
기존 Settlement를 수정하거나 취소하지 않고,
보정 Settlement(ADJUSTMENT)를 추가 생성하여 처리한다.

---

## 6. 조정 정산 규칙

정산 완료 이후의 예외 상황은 다음 방식으로 처리한다.

- 기존 Settlement는 유지한다.
- ADJUSTMENT 타입의 Settlement를 새로 생성한다.
- ADJUSTMENT Settlement는 원본 Settlement를 참조한다.
- 보정 금액은 음수 또는 양수로 표현될 수 있다.

이를 통해 모든 정산 이력은 추적 가능하며,
회계 운영 정책 변경에 대응가능하다.

---

## 7. 디자인 원칙

- Settlement는 과거 사실을 기록하는 도메인이다.
- Settlement는 실시간 사용자 정보나 결제 상태를 참조하지 않는다.
- 모든 정산 변경은 추가 기록 방식으로만 처리한다.