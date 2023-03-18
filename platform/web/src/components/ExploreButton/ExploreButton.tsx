import { click } from '@testing-library/user-event/dist/click';
import { Button } from 'react-bootstrap';
import './styles.css';

export default function ExploreButton({ onButtonClick }: { onButtonClick: (event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => void }) {
    return (
        <div>
            <div className="button-demo css-fizzy-button">
                <div className="demo-buttons-group">
                    <div className='button'>
                        <input id='button' type='checkbox' onClick={(e: any) => onButtonClick(e)} />
                        <label htmlFor='button' placeholder='Explore'>
                            <div className='button_inner q'>
                                <p>Explore</p>
                                <div className='b_l_quad' id='button'>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                    <div className='button_spots'></div>
                                </div>
                            </div>
                        </label>
                    </div>
                </div>
            </div>
        </div>
    );
}
