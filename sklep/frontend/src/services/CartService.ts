import axios from 'axios';

const HOST = 'http://localhost:9000'

export const addCart = async (id: number, providerKey: string, productId: number, amount: number) => {
    return axios.post(`${HOST}/cart/create`, {
        id: id,
        providerKey: providerKey,
        productId: productId,
        amount: amount
    })
}

export const deleteCart = async (id: number) => {
    console.log(id)
    return axios.delete(`${HOST}/cart/delete/${id}`)
}


export const listCart = () => {
    return axios.get(`${HOST}/cart/read-all`)
}

export const getCartByUser = async (providerKey: string | undefined) => {
    return axios.get(`${HOST}/cart/read-by-user/${providerKey}`)
}

export const getCart = async (id: number) => {
    return axios.get(`${HOST}/cart/read/${id}`)
}