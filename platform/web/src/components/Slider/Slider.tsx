
import Carousel from 'react-bootstrap/Carousel';
import dubai from '../../assets/dubai_pic.jpg';
import italy from '../../assets/italy_pic.jpg';
import swiss from '../../assets/swiss_pic.jpg';
import './styles.css';


export default function Slider() {
    const images: string[] = [dubai, italy, swiss];
    return (
        <Carousel indicators={false} controls={false} className='slider'>
            {images.map((image, index) =>
                <Carousel.Item
                    key={index}
                    style={{
                        backgroundImage: `url(${image})`,
                    }}
                    className='background'
                >
                </Carousel.Item>
            )}
        </Carousel>
    );
}
