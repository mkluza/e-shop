import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';
import {addCart, deleteCart, getCartByUser, getCart} from '../services/CartService';


export interface CartProps {
	id: number,
	providerKey: string,
	productId: number,
	amount: number,
}

interface ICartStore {
	carts: CartProps[]
}

export class CartStore implements ICartStore {
	private rootStore: RootStore | undefined;

	carts: CartProps[] = [];
	total_price: number = 0
	cart: CartProps = {id: 1, providerKey:'', productId: 1, amount:1}

	constructor(rootStore?: RootStore) {
		makeAutoObservable(this)
		this.rootStore = rootStore;
	}

	showPrice = () => {
		return this.total_price
	}


	addCart = async (cart: CartProps, price: string) => {
		let amount = 0
		let idx = -1

		this.carts.forEach(c => {
			if (c.productId == cart.productId) {
				idx = c.id
				amount = c.amount
			}
		})

		if(idx != -1)
		{
			await this.deleteCart(idx, price)
		}
		await addCart(cart.id, cart.providerKey, cart.productId, amount+1)
		this.total_price += Number(price)*(amount+1)
		await this.listCarts(cart.providerKey)
	}


	deleteCart = async (id: number, price: string) => {
		const result = await getCart(id)
		await deleteCart(id)
		const idx = this.carts.findIndex((entry)=> entry.id == id)
		this.carts = [
			 			...this.carts.slice(0, idx),
			 			...this.carts.slice(idx + 1, this.carts.length),
			 		]
		this.cart = {...result.data}
		const x = Number(price)*this.cart.amount
		if(x >= this.total_price)
		{
			this.total_price -= this.total_price
		}
		else this.total_price -= x
	}

	listCarts = async (providerKey: string | undefined) => {
			const cartList = await getCartByUser(providerKey)
			this.carts = cartList.data.map((cart: CartProps) => {
				return {
					id: cart.id,
					providerKey: cart.providerKey,
					productId: cart.productId,
					amount: cart.amount,
				}
			})
		return this.carts
	}

}
