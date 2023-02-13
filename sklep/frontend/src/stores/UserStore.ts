import {RootStore} from './RootStore';
import {makeAutoObservable} from 'mobx';


export interface UserProps {
    providerKey: string | undefined,
}


export class UserStore {
    private rootStore: RootStore | undefined;

    loggedUser: UserProps = {providerKey: ''}

    constructor(rootStore?: RootStore) {
        makeAutoObservable(this)
        this.rootStore = rootStore;
    }

    loginUser = (user: UserProps) => {
        this.loggedUser = {...user}
        return this.loggedUser

    }

    showUser = () => {
        return this.loggedUser
    }

    logoutUser = () => {
        this.loggedUser = {providerKey: ''}
        return this.loggedUser
    }
}