import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';
import {ProductProps} from '../components/Product';
import {listProducts} from '../services/ProductService';
import {listCategories} from '../services/CategoryService';

export interface CategoryProps {
	id: number,
	name: string,
}

interface IProductsStore {
	products: ProductProps[]
	categories: CategoryProps[]
}

export class ProductsStore implements IProductsStore {
	private rootStore: RootStore | undefined;

	products: ProductProps[] = [];
	categories: CategoryProps[] = [];

	constructor(rootStore?: RootStore) {
		makeAutoObservable(this)
		this.rootStore = rootStore;
	}
	
	listProducts = async () => {
		if (this.products.length === 0) {
			const productList = await listProducts()
			this.products = productList.data.map((product: ProductProps) => {
				return  {
					id: product.id,
					categoryId: product.categoryId,
					name: product.name,
					image: product.image,
					description: product.description,
					price: product.price,
				}
			})
		}
		return this.products
	}

	listCategory = async () => {
		if (this.categories.length === 0) {
			const categoryList = await listCategories()
			console.log(categoryList)
			this.categories = categoryList.data.map((category: CategoryProps) => {
				return  {
					id: category.id,
					name: category.name,
				}
			})
		}
		return this.categories
	}
}
