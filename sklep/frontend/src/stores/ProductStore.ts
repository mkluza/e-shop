import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';
import {ProductProps} from '../components/Product';
import {getProduct} from "../services/ProductService";


export class ProductStore {
    private rootStore: RootStore | undefined;

    checkedProduct: ProductProps = {id: 1, name: '', categoryId: 1, description: '', image: '', price: ''}

    constructor(rootStore?: RootStore) {
        makeAutoObservable(this)
        this.rootStore = rootStore;
    }

    checkProduct = async (id: number) => {
        const product = await getProduct(id)
        this.checkedProduct = {...product.data}
    }


    showProduct = () => {
        return this.checkedProduct
    }
}
