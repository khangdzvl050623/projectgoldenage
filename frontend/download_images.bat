@echo off
echo Downloading images...

:: Create directories if they don't exist
mkdir "public\img\portfolio" 2>nul
mkdir "public\img\testimonials" 2>nul
mkdir "public\img\team" 2>nul

:: Download portfolio images
echo Downloading portfolio images...
curl -o "public\img\portfolio\necklace-large.jpg" "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338"
curl -o "public\img\portfolio\necklace-small.jpg" "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338"
curl -o "public\img\portfolio\ring-large.jpg" "https://images.unsplash.com/photo-1573408301185-9146fe634ad0"
curl -o "public\img\portfolio\ring-small.jpg" "https://images.unsplash.com/photo-1573408301185-9146fe634ad0"
curl -o "public\img\portfolio\bracelet-large.jpg" "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f"
curl -o "public\img\portfolio\bracelet-small.jpg" "https://images.unsplash.com/photo-1599643478518-a784e5dc4c8f"

:: Download testimonial images
echo Downloading testimonial images...
curl -o "public\img\testimonials\01.jpg" "https://randomuser.me/api/portraits/women/1.jpg"
curl -o "public\img\testimonials\02.jpg" "https://randomuser.me/api/portraits/men/1.jpg"

:: Download team images
echo Downloading team images...
curl -o "public\img\team\01.jpg" "https://randomuser.me/api/portraits/women/2.jpg"
curl -o "public\img\team\02.jpg" "https://randomuser.me/api/portraits/men/2.jpg"

:: Download intro background
echo Downloading intro background...
curl -o "public\img\intro-bg.jpg" "https://images.unsplash.com/photo-1515562141207-7a88fb7ce338"

echo Download complete!
pause 