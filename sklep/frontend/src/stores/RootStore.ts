import {CartStore} from './CartStore';
import {ProductsStore} from './ProductsStore';
import {ProductStore} from './ProductStore';
import {UserStore} from './UserStore';
import {AddressStore} from './AddressStore';
import {OrderStore} from './OrderStore';

export class RootStore {
	cartStore: CartStore;
	productsStore: ProductsStore;
	productStore: ProductStore;
	userStore: UserStore;
	addressStore: AddressStore;
	orderStore: OrderStore;


	constructor() {
		this.cartStore = new CartStore(this);
		this.productsStore = new ProductsStore(this);
		this.productStore = new ProductStore(this);
		this.userStore = new UserStore(this);
		this.addressStore = new AddressStore(this);
		this.orderStore = new OrderStore(this);
	}
}
