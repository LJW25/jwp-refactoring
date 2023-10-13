package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.Fixture;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class OrderServiceTest extends ServiceIntegrationTest {
    private static final Order ORDER_STATUS_COOKING = new Order("COOKING");

    @Autowired
    private OrderService orderService;

    @Autowired
    private TableService tableService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductService productService;

    @Autowired
    private MenuGroupService menuGroupService;

    private Menu menu;

    @BeforeEach
    void setUp() {
        final MenuGroup menuGroup = menuGroupService.create(Fixture.MENU_GROUP);
        final Product product = productService.create(Fixture.PRODUCT);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 2);
        menu = menuService.create(new Menu("후라이드+후라이드",
                BigDecimal.valueOf(19000),
                menuGroup.getId(),
                List.of(menuProduct)));
    }

    @Test
    void create() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final Order order = new Order(orderTable.getId(), List.of(orderLineItem));

        // when
        final Order result = orderService.create(order);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isNotNull();
            softly.assertThat(result.getOrderedTime()).isNotNull();
            softly.assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
        });
    }

    @Test
    void create_duplicatedMenuException() {
        // given
        final OrderLineItem orderLineItem1 = new OrderLineItem(menu.getId(), 1);
        final OrderLineItem orderLineItem2 = new OrderLineItem(menu.getId(), 2);
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final Order order = new Order(orderTable.getId(), List.of(orderLineItem1, orderLineItem2));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableNullException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final Order order = new Order(-1L, List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void create_tableEmptyException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_EMPTY);
        final Order order = new Order(orderTable.getId(), List.of(orderLineItem));

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final OrderTable orderTable1 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final OrderTable orderTable2 = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);

        orderService.create(new Order(orderTable1.getId(), List.of(orderLineItem)));
        orderService.create(new Order(orderTable2.getId(), List.of(orderLineItem)));

        // when
        final List<Order> result = orderService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL", "COMPLETION"})
    void changeOrderStatus(final String status) {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final Order saved = orderService.create(new Order(orderTable.getId(), List.of(orderLineItem)));

        // when
        final Order changed = new Order(status);
        final Order result = orderService.changeOrderStatus(saved.getId(), changed);

        // then
        assertThat(result.getOrderStatus()).isEqualTo(status);
    }

    @Test
    void changeOrderStatus_orderNullException() {
        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(-1L, ORDER_STATUS_COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeOrderStatus_orderCompletedException() {
        // given
        final OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1);
        final OrderTable orderTable = tableService.create(Fixture.ORDER_TABLE_NOT_EMPTY);
        final Order saved = orderService.create(new Order(orderTable.getId(), List.of(orderLineItem)));
        orderService.changeOrderStatus(saved.getId(), new Order(OrderStatus.COMPLETION.name()));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(saved.getId(), ORDER_STATUS_COOKING))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
