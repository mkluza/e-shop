import axios from 'axios';

const HOST = 'http://localhost:9000'

export const listCategories = async () => {
	return axios.get(`${HOST}/category/read-all`)
}
