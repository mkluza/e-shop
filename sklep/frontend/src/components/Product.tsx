// @ts-ignore
import React, {FC} from 'react';
import styled from 'styled-components';
import {
	Card,
	CardActionArea,
	CardContent,
	CardMedia,
	Typography
} from '@material-ui/core';
import {RootStore} from '../stores/RootStore';
import {inject, observer} from 'mobx-react';
import {useHistory} from 'react-router';


export interface ProductProps {
	id: number,
	categoryId: number,
	name: string,
	image: string,
	description: string,
	price: string
}

export const Product: FC<{store?: RootStore} & ProductProps> = inject("store")(observer(({
	store, id, categoryId, name, image, description, price
},) => {

	const history = useHistory()

	const productStore = store?.productStore

	const handleClick = () => {
		productStore?.checkProduct(id)
		history.push(`/product/${id}`)
	}

	return (
		<ProductStyled>
			<Card style={{backgroundColor: 'rgb(131,163,255)'}}>
				<CardActionArea onClick={() => handleClick()}>
					<CardMedia
						component="img"
						height="250"
						image={image}
						title={name}
					/>
					<CardContent>
						<Typography gutterBottom variant="h4" component="h2" style={{color: 'white', textAlign: 'center', fontWeight: 600}}>
							{name}
						</Typography>

					</CardContent>
				</CardActionArea>
			</Card>
		</ProductStyled>
	);
}));

const ProductStyled = styled.div`
	padding: 10px;
`;