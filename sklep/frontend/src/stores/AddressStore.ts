import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';
import {addAddress, getAddress, listAddress, updateAddress} from '../services/AddressService';

export interface AddressProps {
    id: number,
    providerKey: string | undefined,
    firstname: string,
    lastname: string,
    city: string,
    zipcode: string,
    street: string,
    phoneNumber: string,
}


export class AddressStore  {
    private rootStore: RootStore | undefined;

    address: AddressProps = {id: 1, providerKey: '', firstname: '', lastname: '', city: '', zipcode: '', street: '', phoneNumber: ''};

    constructor(rootStore?: RootStore) {
        makeAutoObservable(this)
        this.rootStore = rootStore;
    }

    addAddress = async (providerKey: string | undefined) => {
        if(!await this.existAddress(providerKey))
        {
            await addAddress(1, providerKey, '', '', '', '', '', '')
        }
        console.log(this.address)
        const address = await getAddress(providerKey)
        this.address = {...address.data}
    }

    setAddress = async (providerKey: string | undefined) => {
        const address = await getAddress(providerKey)
        this.address = {...address.data}
    }

    showAddress = () => {
        return this.address
    }

    updateAddress = async (address: AddressProps) => {
        await updateAddress(address.id, address.providerKey, address.firstname, address.lastname, address.city, address.zipcode, address.street, address.phoneNumber)
        this.address = {...address}
    }

    existAddress = async (providerKey: string | undefined) => {
        const addressList = await listAddress()
        let exist = false
        console.log(addressList)
        addressList.data.map((address: AddressProps) => {
                if(address.providerKey == providerKey)
                {
                    exist = true
                }
            })
        return exist
    }
}
