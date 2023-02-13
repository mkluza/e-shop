import axios from 'axios';

const HOST = 'http://localhost:9000'

export const listProducts = async () => {
	return axios.get(`${HOST}/product/read-all`)
}

export const getProduct = async (id:number) => {
	return axios.get(`${HOST}/product/read/${id}`)
}

export const listProductsByCategory = async (id:number) => {
	return axios.get(`${HOST}/product/read-by-category/${id}`)
}
