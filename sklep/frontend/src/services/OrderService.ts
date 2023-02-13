import axios from 'axios';

const HOST = 'http://localhost:9000'

export const addOrder = async (id: number, cartId: number, addressId: number) => {
	return axios.post(`${HOST}/order/create`, {
		id: id,
		cartId: cartId,
		addressId: addressId
	})
}

export const listOrders = async () => {
	return axios.get(`${HOST}/order/read-all`)
}
