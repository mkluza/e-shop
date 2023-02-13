import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';
import {addOrder, listOrders} from '../services/OrderService';


export interface OrderProps {
    id: number,
    cartId: number,
    addressId: number,
}

interface IOrderStore {
    orders: OrderProps[]
}

export class OrderStore implements IOrderStore {
    private rootStore: RootStore | undefined;

    orders: OrderProps[] = [];

    constructor(rootStore?: RootStore) {
        makeAutoObservable(this)
        this.rootStore = rootStore;
    }

    addOrder = async (order: OrderProps) => {
        await addOrder(order.id, order.cartId, order.addressId)

    }

    listOrders = async () => {
        const orderList = await listOrders()
        this.orders = orderList.data.map((order: OrderProps) => {
            return {
                id: order.id,
                cartId: order.cartId,
                addressId: order.addressId,
            }
        })
        return this.orders
    }
}
