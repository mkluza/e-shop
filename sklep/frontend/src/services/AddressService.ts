import axios from 'axios';

const HOST = 'http://localhost:9000'

export const addAddress = async (id: number, providerKey: string | undefined, firstname: string, lastname: string, city: string, zipcode: string, street: string, phoneNumber: string) => {
	return axios.post(`${HOST}/address/create`, {
		id: id,
		providerKey: providerKey,
		firstname: firstname,
		lastname: lastname,
		city: city,
		zipcode: zipcode,
		street: street,
		phoneNumber: phoneNumber,
	})
}

export const getAddress = async (providerKey: string | undefined) => {
	return axios.get(`${HOST}/address/read/${providerKey}`)
}

export const listAddress = () => {
	return axios.get(`${HOST}/address/read-all`)
}

export const updateAddress = async (id: number, providerKey: string | undefined, firstname: string, lastname: string, city: string, zipcode: string, street: string, phoneNumber: string) => {
	return axios.put(`${HOST}/address/update`, {
		id: id,
		providerKey: providerKey,
		firstname: firstname,
		lastname: lastname,
		city: city,
		zipcode: zipcode,
		street: street,
		phoneNumber: phoneNumber,
	})
}